package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "OFFLINE_STATE")
public class OfflineState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id") // UserVO의 id를 참조
    private UserVO user;

    @Enumerated(EnumType.STRING)
    @Column(name = "offline_state")
    private State state;

    public enum State {
        ONLINE, OFFLINE
    }

    @Column(name = "last_active")
    private LocalDateTime lastActive;
}
