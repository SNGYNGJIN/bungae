package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="MESSAGE_IN_OFFLINE")
public class MessageInOffline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message")
    private String message;

    @Column(name = "sender_nickname")
    private String senderNickname;

    @Column(name = "sended_time")
    private LocalDateTime sendedTime;

    @Column(name = "is_read")
    private boolean isRead;
}
