package com.multi.bungae.domain;

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
    @JoinColumn(name = "user_id") // 리뷰를 받는 유저 ID
    private UserVO user;

    @Column(name = "content")
    private String content;

    @Column(name = "rating", nullable = false)
    private int rating;
}
