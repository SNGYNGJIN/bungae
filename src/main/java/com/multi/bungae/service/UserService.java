package com.multi.bungae.service;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseExceptionStatus;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.domain.BaseVO;
import com.multi.bungae.dto.user.*;
import com.multi.bungae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import static com.multi.bungae.config.BaseExceptionStatus.INVALID_PASSWORD;
import static com.multi.bungae.utils.ValidationRegex.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CheckIdRes checkId(CheckIdReq checkIdReq) {
        return new CheckIdRes(!userRepo.existsUserByUserId(checkIdReq.getUserId()));
    }


    @Transactional
    public SignupRes signupRes(@RequestBody SignupReq signupReq) throws BaseException {

        if (signupReq.getUserId() == null || signupReq.getUserId().isEmpty()) {
            // userId가 null이거나 빈 문자열인 경우 처리
            System.out.println("userId값이 "+signupReq.getUserId() + "입니다.");
            throw new BaseException(BaseExceptionStatus.EMPTY_ID);
        }
        // id 빈 값인지 검사
        // id 정규표현: 입력받은 id가 영문 대소문자,숫자 4-16자리 형식인지 검사
        if (signupReq.getUserId().isEmpty() || !isRegexId(signupReq.getUserId())) {
            throw signupReq.getUserId().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_ID) :
                    new BaseException(BaseExceptionStatus.INVALID_ID);
        }

        // password 빈 값인지 검사
        // 비밀번호 정규표현: 입력받은 비밀번호가 숫자, 특문 각 1회 이상, 영문은 대소문자 모두 사용하여 8~16자리 입력과 같은 형식인지 검사
        if (signupReq.getPasswd().isEmpty() || !isRegexPassword(signupReq.getPasswd())) {
            throw signupReq.getPasswd().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_PASSWORD) :
                    new BaseException(INVALID_PASSWORD);
        }

        // 닉네임 빈 값인지 검사
        // 닉네임 정규표현: 입력받은 닉네임이 숫자와 영문, 한글로만 이루어졌는지 검사
        if (signupReq.getNickname().isEmpty() || !isRegexNickname(signupReq.getNickname())) {
            throw signupReq.getNickname().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_NICKNAME) :
                    new BaseException(BaseExceptionStatus.INVALID_NICKNAME);
        }

        // email 빈 값인지 검사
        // 이메일 정규표현: 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사
        if (signupReq.getEmail().isEmpty() || !isRegexEmail(signupReq.getEmail())) {
            throw signupReq.getEmail().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_EMAIL) :
                    new BaseException(BaseExceptionStatus.INVALID_EMAIL);
        }

        // 이름 빈 값인지 검사
        // 입력받은 이름이 한글로만 이루어졌는지 검사
        if (signupReq.getName().isEmpty() || !isRegexName(signupReq.getName())) {
            throw signupReq.getName().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_NAME) :
                    new BaseException(BaseExceptionStatus.INVALID_NAME);
        }

        // 생년월일 빈 값인지 검사
        // 입력받은 생년월일이 숫자로만 이루어졌는지 검사
        if (signupReq.getBirth().isEmpty() || !isRegexBirthDate(signupReq.getBirth())) {
            throw signupReq.getBirth().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_BIRTH) :
                    new BaseException(BaseExceptionStatus.INVALID_BIRTH);
        }

        // 전화번호 빈 값인지 검사
        // 입력받은 번호가 숫자로만 이루어졌는지 검사
        if (signupReq.getTel().isEmpty() || !isRegexTel(signupReq.getTel())) {
            throw signupReq.getBirth().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_TEL) :
                    new BaseException(BaseExceptionStatus.INVALID_TEL);
        }

        // 성별 빈 값인지 검사
        // 입력받은 성별이 FEMALE, MALE로만 이루어졌는지 검사
        if (signupReq.getGender()==null || !isRegexGender(signupReq.getGender().name())) {
            throw signupReq.getBirth().isEmpty() ?
                    new BaseException(BaseExceptionStatus.EMPTY_GENDER) :
                    new BaseException(BaseExceptionStatus.INVALID_GENDER);
        }

        UserVO user = UserVO.builder()
                .userId(signupReq.getUserId())
                .password(passwordEncoder.encode(signupReq.getPasswd()))
                .nickname(signupReq.getNickname())
                .email(signupReq.getEmail())
                .username(signupReq.getName())
                .userBirth(signupReq.getBirth())  // 생년월일 문자열 직접 사용
                .tel(signupReq.getTel())
                .userGender(signupReq.getGender())
                .build();

        userRepo.save(user);
        return new SignupRes(user.getId(), signupReq.getUserId());
    }


    public FindIdRes findId(FindIdReq findIdReq) throws BaseException {
        List<UserVO> user_list = userRepo.findByEmail(findIdReq.getEmail());
        if (user_list.isEmpty()){
            throw new BaseException(BaseExceptionStatus.NOT_FOUND_EMAIL);
        }
        return new FindIdRes(user_list.get(0).getId());
    }

    public LoginRes login(LoginReq loginReq) throws BaseException {
        return new LoginRes("d", "d");
    }

}