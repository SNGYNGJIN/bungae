package com.multi.bungae.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.service.ChatService;
import lombok.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class ChatDTO {

    private Long chatRoomId;
    private String sender;
    private String message;
    private ChatMessage.MessageType type;
    private LocalDateTime sendTime;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatDTO(Long chatRoomId, String sender, String message, ChatMessage.MessageType type, LocalDateTime sendTime, Set<WebSocketSession> sessions) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.sendTime = sendTime;
        this.sessions = sessions;
    }

    // 새로운 생성자 추가
    public ChatDTO(Long chatRoomId, String sender, String message, ChatMessage.MessageType type, LocalDateTime sendTime) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.sendTime = sendTime;
    }
}