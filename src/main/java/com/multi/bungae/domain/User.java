package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false, nullable = false)
    private Timestamp createAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    public enum Gender {
        FEMALE, MALE
    }

    public enum Status {
        ACTIVE, INACTIVE, DELETE
    }
}
