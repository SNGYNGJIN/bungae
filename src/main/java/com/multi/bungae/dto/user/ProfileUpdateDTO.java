package com.multi.bungae.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    private String nickname;   // UserVO의 닉네임
    private String userInfo;   // UserProfile의 자기소개
    private String userImage;  // 유저 이미지 파일 경로
}

