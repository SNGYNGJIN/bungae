package com.multi.bungae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.repository.UserRepository;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
@Slf4j
@Data
@Service
public class ChatService {
    private final ObjectMapper mapper;
    private Map<Long, ChatDTO> chatRooms = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(WebSocketChatHandler.class);
    private final ChatMessageRepository chatMessageRepo;
    private final Map<String, Set<WebSocketSession>> chatSessions = new ConcurrentHashMap<>();
    @Autowired
    private BungaeRepository bungaeRepo;
    @Autowired
    private UserRepository userRepo;

    public List<ChatDTO> findAllRoom(){
        return new ArrayList<>(chatRooms.values());
    }

    public ChatDTO findRoomById(Long roomId){
        return chatRooms.get(roomId);
    }


    /*
        번개 모임을 개설할 때 주최자는 Type: ENTER 로 바로 입장
    */
    public ChatDTO createChat(Long chatRoomId, String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        String nickname = user.getNickname();
        String bungaeName = bungae.getBungaeName();

        ChatDTO chat = ChatDTO.builder()
                .chatRoomId(chatRoomId)
                .sender(userId)
                .message("[" + nickname + "]님이 <" + bungaeName + ">을 개설하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .build();

        // DTO를 Entity로 변환하기
        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);

        return chat;
    }

    /*
        번개 모임에 참여할 때 유저는 Type: ENTER 로 입장
    */
    public ChatDTO joinChat(Long chatRoomId, String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        String nickname = user.getNickname(); // userId 기준으로 찾아서 닉네임 대입
        String bungaeName = bungae.getBungaeName();

        ChatDTO chat = ChatDTO.builder()
                .chatRoomId(chatRoomId)
                .sender(userId) // userId 대입 ex)user1234
                .message("[" + nickname + "]님이 <" + bungaeName + ">에 참가하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .build();

        // DTO를 Entity로 변환하기
        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);

        return chat;
    }

    public ChatDTO ChatMessage(Long chatRoomId, String senderId, String message) {
        UserVO sender = userRepo.findByUserId(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Bungae chatRoom = bungaeRepo.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("Chat room not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setMessage(message);
        chatMessage.setType(ChatMessage.MessageType.TALK);

        chatMessageRepo.save(chatMessage);

        return new ChatDTO(chatRoomId, senderId, message, ChatMessage.MessageType.TALK);
    }

    private ChatMessage convertToEntity(ChatDTO chatDTO) {
        ChatMessage chatMessage = new ChatMessage();

        // Bungae 엔티티 찾기
        Bungae bungae = bungaeRepo.findById(chatDTO.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Bungae not found"));

        // User 엔티티 찾기
        UserVO user = userRepo.findByUserId(chatDTO.getSender())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        chatMessage.setChatRoomId(bungae);
        chatMessage.setSender(user);
        chatMessage.setMessage(chatDTO.getMessage());
        chatMessage.setType(chatDTO.getType());


        return chatMessage;
    }

    /**
     * 채팅방에 접근하여 세션을 반환
     * @param chatRoomId 채팅방 ID
     * @param session 웹소켓 세션
     */
    public void accessRoom(Long chatRoomId, WebSocketSession session) {
        log.info("Accessing chat room: {}", chatRoomId);
        Set<WebSocketSession> sessions = chatSessions.computeIfAbsent(chatRoomId.toString(), k -> ConcurrentHashMap.newKeySet());
        sessions.add(session);

        log.info("Session added to chat room: {}", chatRoomId);
        // 해당 세션에 대한 초기 메시지나 설정을 할 수 있습니다.
        sendMessage(session, new TextMessage("입장 완 : " + chatRoomId));


    }
    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("Error sending message", e);
        }
    }


}