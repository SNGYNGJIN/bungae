package com.multi.bungae.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.apache.tomcat.util.net.AbstractEndpoint;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SocketStateDTO {

    private Long chatRoomId;
    private int userId;
    private AbstractEndpoint.Handler.SocketState state;
    private LocalDateTime closedTime;

    public SocketStateDTO(int userId, LocalDateTime closedTime) {
        this.userId = userId;
        this.closedTime = closedTime;
    }
}
