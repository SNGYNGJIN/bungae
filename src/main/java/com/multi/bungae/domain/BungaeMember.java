package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BungaeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bungaeMemberId;

    @ManyToOne
    @JoinColumn(name = "bungae_id")
    private Bungae bungae;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserVO user;

    private boolean isOrganizer;
}
