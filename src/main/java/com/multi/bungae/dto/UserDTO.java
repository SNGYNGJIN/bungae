package com.multi.bungae.dto;

import com.multi.bungae.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id; // 오라클은 시퀀스,mysql은 auto-increment
    private String userId;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String name;
    private String birth;

    @Enumerated(EnumType.STRING)
    private User.Gender gender;

    private String tel;

    private Timestamp createAt;

    @Enumerated(EnumType.STRING)
    private User.Status status;

    //@Builder
    public UserDTO(Integer id, String userId, String username, String password, String nickname,
                   String email, String name, String birth, User.Gender gender, Timestamp createAt,
                   String tel, User.Status status) {
        this.id=id;
        this.userId=userId;
        this.username=username;
        this.password=password;
        this.nickname=nickname;
        this.email=email;
        this.name=name;
        this.birth=birth;
        this.gender= gender;
        this.createAt=createAt;
        this.tel=tel;
        this.status=status;
    }


    //toEntity()메서드를 통해 Service > Database(Entity)로 Data를 전달할 때 Dto를 통해서 전달
    public User toEntity() {
        User user = User.builder()
                .id(id)
                .userId(userId)
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .userBirth(birth)
                .userGender(gender)
                .createAt(createAt)
                .tel(tel)
                .status(status)
                .build();
        return user;
    }
}