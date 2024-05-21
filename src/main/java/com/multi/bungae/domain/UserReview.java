package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER_REVIEW")
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 리뷰를 받는 유저 ID Int로 관리
    @JsonBackReference
    private UserProfile user;

    @Column(name = "reviewer_id")
    private int reviewerId;

    @Column(name = "content")
    private String content;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "bungae_id")
    private Long bungaeId;
}
