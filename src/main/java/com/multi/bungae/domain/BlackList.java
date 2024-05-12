package com.multi.bungae.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "blackList")
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blackListId")
    private int blackListId;

    @ManyToOne
    @JoinColumn(name = "blockerUserProfileId", nullable = false)
    private UserProfile blockerUserProfile;

    @ManyToOne
    @JoinColumn(name = "blockedUserProfileId", nullable = false)
    private UserProfile blockedUserProfile;
}
