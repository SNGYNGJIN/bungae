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

    @Column(name = "bungae_id")
    private Long bungaeId;

    @Column(name = "user_id")
    private int user;

    private boolean isOrganizer;
}
