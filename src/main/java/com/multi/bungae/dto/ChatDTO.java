package com.multi.bungae.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.service.ChatService;
import lombok.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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
    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatDTO(Long chatRoomId, String sender, String message, ChatMessage.MessageType type, Set<WebSocketSession> sessions) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.sessions = sessions;
    }

    // 새로운 생성자 추가
    public ChatDTO(Long chatRoomId, String sender, String message, ChatMessage.MessageType type) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.type = type;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service) {
        // 모든 메시지에 대해 세션 추가, 중복 세션 방지
        sessions.add(session);

        // 메시지 타입에 따라 처리
        if (message.getType().equals(ChatMessage.MessageType.ENTER)) {
            message.setMessage(message.getSender() + " 님이 입장하셨습니다.");
        }

        sendMessage(message);
    }

    // 메시지를 모든 세션에 전송
    public void sendMessage(ChatDTO message) {
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(message)));
                }
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
                sessions.remove(session); // 오류가 발생한 세션 제거
            }
        });
    }
}

