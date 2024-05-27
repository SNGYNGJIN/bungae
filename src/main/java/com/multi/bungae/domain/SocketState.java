package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.util.net.AbstractEndpoint;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="SOCKET_STATE")
public class SocketState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AbstractEndpoint.Handler.SocketState state;

    @Column(name = "closed_time") // 채팅 발송 시간
    private LocalDateTime closedTime;

}


