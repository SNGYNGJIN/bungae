package com.multi.bungae.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "my_page")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false, unique = true)
    private int profileId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_id"))
    private UserVO user;

    @Column(name = "user_avg_rating", nullable = false)
    private double userRating;

    @Column(name = "user_age", nullable = false)
    private int userAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private UserVO.Gender gender;

    @Column(name = "user_intro")
    private String userIntro;

    @Column(name = "user_image")
    private String userImage; // UUID를 String으로 저장
}
