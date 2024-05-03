package com.multi.bungae.dto.user;

import com.multi.bungae.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRes {
    private String id;
    private String userId;
    private String nickname;
    private String name;
    private String birth;
    private UserVO.Gender gender;
    private String email;
    private String tel;

    public SignupRes(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }
}
