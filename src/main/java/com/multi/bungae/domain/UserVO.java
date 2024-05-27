package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER")
public class UserVO extends BaseVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private UserProfile profile;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_nickName", nullable = false)
    private String nickname;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_birth_date", nullable = false)
    private String userBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private Gender userGender;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name = "user_email", nullable = false)
    private String email;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private OfflineState state;

    public enum Gender {
        FEMALE, MALE
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<BungaeMember> bungaeMembers = new HashSet<>();
}