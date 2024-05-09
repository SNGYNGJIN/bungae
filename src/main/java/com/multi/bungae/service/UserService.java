package com.multi.bungae.service;

import com.multi.bungae.dto.UserDTO;
import com.multi.bungae.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    //생성자 주입
    private final UserRepo userRepo;

    @Transactional
    //서비스 메서드생성=> 회원가입
    public int signup(UserDTO userDto) {

        try {
            userRepo.save(userDto.toEntity());
            return 100 ; // 리턴되는 값 json으로 값 넘겼을 때 보여지는 부분 user.js에 콘솔창 결과값나옴
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("userservice " + e.getMessage());
        }
        return -100;
    }


}