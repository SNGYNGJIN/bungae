package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bungae_id")
    @JsonBackReference
    private Bungae bungae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserVO user;

    private boolean isOrganizer;
}
