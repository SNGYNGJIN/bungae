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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "bungae_id") // Bungae 엔티티 참조
    private Bungae chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id") // UserVO 엔티티 참조
    private UserVO sender;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입: 입장, 채팅

    @CreationTimestamp
    @Column(name = "send_time", updatable = false, nullable = false) // 채팅 발송 시간
    private LocalDateTime sendTime;

    public enum MessageType {
        ENTER, TALK
    }
}


