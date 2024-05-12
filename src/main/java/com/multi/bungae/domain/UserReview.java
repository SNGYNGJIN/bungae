package com.multi.bungae.domain;

import jakarta.persistence.*;

public class UserReview {

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_profile_id"))
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private UserProfile myPage;

    @Column(name = "content")
    private String content;

    @Column(name = "rating", nullable = false)
    private int rating;
}
