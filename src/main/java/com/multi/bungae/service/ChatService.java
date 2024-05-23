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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;

@Slf4j
@Data
@Service
public class ChatService {
    private SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper mapper;
    private Map<Long, ChatDTO> chatRooms = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(WebSocketChatHandler.class);
    @Autowired
    private final ChatMessageRepository chatMessageRepo;
    private final Map<String, Set<WebSocketSession>> chatSessions = new ConcurrentHashMap<>();
    @Autowired
    private BungaeRepository bungaeRepo;
    @Autowired
    private BungaeMemberRepository bungaememberRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private final BungaeMemberServiceImpl bungaeMemberService;
    @Autowired
    private final SocketStateService socketStateService;


    /*
        번개 모임을 개설할 때 주최자는 Type: ENTER 로 바로 입장
    */
    public ChatDTO createChat(Long chatRoomId, String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        String nickname = user.getNickname();
        String bungaeName = bungae.getBungaeName();
        LocalDateTime sendTime = LocalDateTime.now();

        ChatDTO chat = ChatDTO.builder()
                .chatRoomId(chatRoomId)
                .sender(userId)
                .message("🔈[" + nickname + "]님이 <" + bungaeName + ">을(를) 개설하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .sendTime(sendTime)
                .build();

        // DTO를 Entity로 변환하기
        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);

        socketStateService.createStateOpen(chatRoomId, userId);

        return chat;
    }

    /*
        번개 모임에 참여할 때 유저는 Type: ENTER 로 입장
    */
    public ChatDTO joinChat(Long chatRoomId, String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        String nickname = user.getNickname();
        String bungaeName = bungae.getBungaeName();
        LocalDateTime sendTime = LocalDateTime.now();

        ChatDTO chat = ChatDTO.builder()
                .chatRoomId(chatRoomId)
                .sender(userId)
                .message("🔈 [" + nickname + "]님이 <" + bungaeName + ">에 참가하였습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .sendTime(sendTime)
                .build();

        ChatMessage chatMessage = convertToEntity(chat);
        chatMessageRepo.save(chatMessage);

        socketStateService.createStateOpen(chatRoomId, userId);

        return chat;
    }

    /*
        chatMessage에 저장하고 프론트로 반환
     */
    public ChatDTO ChatMessage(Long chatRoomId, String senderId, String message, ChatMessage.MessageType type) {

        LocalDateTime sendTime = LocalDateTime.now();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setSender(senderId);
        chatMessage.setMessage(message);
        chatMessage.setType(type);
        chatMessage.setSendTime(sendTime);
        chatMessageRepo.save(chatMessage);

        return new ChatDTO(chatRoomId, senderId, message, type, sendTime);
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
        chatMessage.setSendTime(chatDTO.getSendTime());


        return chatMessage;
    }

    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {

        return chatMessageRepo.findByChatRoomId(chatRoomId);
    }

    public boolean checkMemberExists(Long bungae, String userId) {
        return chatMessageRepo.existsByChatRoomIdAndSenderAndType(bungae, userId, ChatMessage.MessageType.ENTER);
    }


}