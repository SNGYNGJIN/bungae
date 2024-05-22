package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="CHAT_MESSAGE")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @JoinColumn(name = "sender_id")
    private String sender;

    @Column(name = "message", length = 300)
    private String message;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입: 입장, 채팅

    @Column(name = "send_time", updatable = false, nullable = false) // 채팅 발송 시간
    private LocalDateTime sendTime;

    public enum MessageType {
        ENTER, TALK
    }
}


