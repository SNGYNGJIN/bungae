package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "USER_PROFILE")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // UserVO의 id를 참조
    private UserVO user;

    @Column(name = "user_avg_rating", nullable = false)
    private double userRating;

    @Column(name = "user_age", nullable = false)
    private int userAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private UserVO.Gender gender;

    @Column(name = "user_info")
    private String userInfo;

    @Column(name = "user_image", length = 1000, nullable = false)
    private String userImage; // UUID를 String으로 저장

    @Builder
    public UserProfile(String userInfo, String userImage) {
        this.userInfo = userInfo;
        this.userImage = userImage;
    }
}
