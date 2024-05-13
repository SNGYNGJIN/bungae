package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BLACKLIST")
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private int blackListId;

    @JoinColumn(name = "blocker_id", nullable = false)
    private String blockerId;

    @JoinColumn(name = "blocked_id", nullable = false)
    private String blockedId;
}
