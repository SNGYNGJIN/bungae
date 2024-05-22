package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    @JsonBackReference
    @JoinColumn(name = "user_id") // UserVO의 id를 참조
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserReview> reviews = new HashSet<>();

}
