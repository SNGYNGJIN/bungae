package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="USER")
public class UserVO extends BaseVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "userId", nullable = false, unique = true)
    private String userId;

    @Column(name = "userPassWord", nullable = false)
    private String password;

    @Column(name = "userNickName", nullable = false)
    private String nickname;

    @Column(name = "userName", nullable = false)
    private String username;

    @Column(name = "userBirthDate", nullable = false)
    private String userBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "userGender", nullable = false)
    private Gender userGender;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name= "userEmail", nullable = false)
    private String email;

    public enum Gender {
        FEMALE, MALE
    }
}
