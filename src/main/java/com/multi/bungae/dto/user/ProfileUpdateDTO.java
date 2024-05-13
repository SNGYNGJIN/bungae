package com.multi.bungae.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    private String nickname;   // UserVO의 닉네임
    private String userInfo;   // UserProfile의 자기소개
    private String userImage;  // UserProfile의 이미지 URL
}
