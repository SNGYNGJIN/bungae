package com.multi.bungae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.controller.ChatController;
import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
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

import static java.lang.Boolean.FALSE;

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
    private BungaeMemberRepository bungaememberRepo;
    @Autowired
    private UserRepository userRepo;
    private final BungaeMemberServiceImpl bungaeMemberService;


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
                .message("🔈[" + nickname + "]님이 <" + bungaeName + ">을(를) 개설하였습니다.")
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
                .message("🔈[" + nickname + "]님이 <" + bungaeName + ">에 참가하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .build();

        // DTO를 Entity로 변환하기
        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);
        bungaeMemberService.createBungaeMember(chatRoomId, user.getId(), FALSE);

        return chat;
    }

    public void ChatMessage(Long chatRoomId, String senderId, String message, ChatMessage.MessageType type) {
        UserVO sender = userRepo.findByUserId(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Bungae chatRoom = bungaeRepo.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("Chat room not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setSender(senderId);
        chatMessage.setMessage(message);
        chatMessage.setType(type);

        chatMessageRepo.save(chatMessage);

        new ChatDTO(chatRoomId, senderId, message, type);
    }

    private ChatMessage convertToEntity(ChatDTO chatDTO) {
        ChatMessage chatMessage = new ChatMessage();

        // Bungae 엔티티 찾기
        Bungae bungae = bungaeRepo.findById(chatDTO.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Bungae not found"));

        // User 엔티티 찾기
        UserVO user = userRepo.findByUserId(chatDTO.getSender())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        chatMessage.setChatRoomId(chatDTO.getChatRoomId());
        chatMessage.setSender(chatDTO.getSender());
        chatMessage.setMessage(chatDTO.getMessage());
        chatMessage.setType(chatDTO.getType());


        return chatMessage;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("Error sending message", e);
        }
    }

/*    public List<ChatMessage> getMessagesByChatRoomId(Bungae chatRoomId) {
        List<ChatMessage> message = chatMessageRepo.findByChatRoomIdOrderBySendTimeAsc(chatRoomId)
        return chatMessageRepo.findByChatRoomIdOrderBySendTimeAsc(chatRoomId);
    }*/
    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {

        return chatMessageRepo.findByChatRoomId(chatRoomId);
    }

    public boolean checkMemberExists(Long bungaeId, int userId) {
        return bungaememberRepo.existsByBungaeIdAndUser(bungaeId, userId);
    }
}