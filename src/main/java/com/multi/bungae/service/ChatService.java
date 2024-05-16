package com.multi.bungae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.ChatRoomDTO;
import com.multi.bungae.repository.BungaeRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.repository.UserRepository;
import groovy.util.logging.Slf4j;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PostLoad;
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
    private Map<Long, ChatRoomDTO> chatRooms = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(WebSocketChatHandler.class);
    private final ChatMessageRepository chatMessageRepo;
    @Autowired
    private BungaeRepository bungaeRepo;
    @Autowired
    private UserRepository userRepo;

    public List<ChatRoomDTO> findAllRoom(){
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDTO findRoomById(Long roomId){
        return chatRooms.get(roomId);
    }

    public ChatRoomDTO createRoom(String name) {
        Long chatRoomId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; // 랜덤한 방 아이디 생성

        // Builder 를 이용해서 ChatRoomDTO를 Building
        ChatRoomDTO room = ChatRoomDTO.builder()
                .chatRoomId(chatRoomId)
                .name(name)
                .build();

        chatRooms.put(chatRoomId, room); // 랜덤 아이디와 room 정보를 Map 에 저장
        return room;
    }

    public ChatDTO createChat(Long chatRoomId, int userId) {
        UserVO user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String nickname = user.getNickname();

        ChatDTO chat = ChatDTO.builder()
                .chatRoomId(chatRoomId)
                .sender(userId)
                .message(nickname + "님이 번개모임을 개설하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .build();

        // DTO를 Entity로 변환하기
        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);

        return chat;
    }

    private ChatMessage convertToEntity(ChatDTO chatDTO) {
        ChatMessage chatMessage = new ChatMessage();

        // Bungae 엔티티 찾기
        Bungae bungae = bungaeRepo.findById(chatDTO.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Bungae not found"));

        // User 엔티티 찾기
        UserVO user = userRepo.findById(chatDTO.getSender())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        chatMessage.setChatRoomId(bungae);
        chatMessage.setSender(user);
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


}