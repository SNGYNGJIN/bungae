package com.multi.bungae.dto.user;

import com.multi.bungae.domain.UserVO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private int id;
    private double userRating;
    private int userAge;
    private UserVO.Gender gender;
    private String userInfo;
    private String userImage;

}
