package com.multi.bungae.dto;

import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.service.ChatService;
import com.multi.bungae.dto.ChatDTO.*;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long chatRoomId; // 방(번개모임) 아이디
    private int sender; // 채팅을 보낸 사람의 ID
    private String message; // 메시지 내용
    private ChatMessage.MessageType type; // 메시지 타입

    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatDTO(Long chatRoomId, int sender, String message, ChatMessage.MessageType type){
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.type = type;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service) {
        // message 에 담긴 타입을 확인한다.
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (message.getType().equals(ChatMessage.MessageType.ENTER)) {
            // sessions 에 넘어온 session 을 담고,
            sessions.add(session);

            // message 에는 입장하였다는 메시지를 띄운다
            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
            sendMessage(message, service);
        } else if (message.getType().equals(ChatMessage.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message, service);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, (ChatDTO) message));
    }
}
