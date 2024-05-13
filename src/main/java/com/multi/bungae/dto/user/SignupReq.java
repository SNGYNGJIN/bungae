package com.multi.bungae.dto.user;

import com.multi.bungae.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupReq {
    private String userId;
    private String passwd;
    private String nickname;
    private String name;
    private String birth;
    private String email;
    private UserVO.Gender gender;
    private String tel;
}