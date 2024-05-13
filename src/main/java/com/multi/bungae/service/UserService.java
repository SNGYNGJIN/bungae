package com.multi.bungae.service;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseExceptionStatus;
import com.multi.bungae.domain.BlackList;
import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.user.*;
import com.multi.bungae.repository.UserProfileRepository;
import com.multi.bungae.repository.UserReviewRepository;
import com.multi.bungae.repository.BlackListRepository;
import com.multi.bungae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

import static com.multi.bungae.config.BaseExceptionStatus.INVALID_PASSWORD;
import static com.multi.bungae.utils.ValidationRegex.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final UserProfileRepository userProfileRepo;
    private final UserReviewRepository userReviewRepo;
    private final BlackListRepository blackListRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return null;
    }


    public CheckIdRes checkId(CheckIdReq checkIdReq) {
        return new CheckIdRes(!userRepo.existsUserByUserId(checkIdReq.getUserId()));
    }

    public LoginRes login(@RequestBody LoginReq loginReq) throws BaseException {
        Optional<UserVO> userOpt = userRepo.findByUserId(loginReq.getUserId());
        if (userOpt.isPresent() && passwordEncoder.matches(loginReq.getPasswd(), userOpt.get().getPassword())) {
            return new LoginRes("access_token_OK", "refresh_token_OK"); // Replace with actual token generation
        } else {
            throw new BaseException(BaseExceptionStatus.LOGIN_FAILED);
        }
    }
    @Transactional
    public SignupRes signupRes(@RequestBody SignupReq signupReq) throws BaseException {

        // 나이 계산
// 나이 계산
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(signupReq.getBirth(), formatter);
        LocalDate currentDate = LocalDate.now();

        int age = currentDate.getYear() - birthDate.getYear();
        if (birthDate.plusYears(age).isAfter(currentDate)) {
            age--;
        }



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

        // UserProfile 생성 및 저장
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setUserAge(age);
        userProfile.setGender(user.getUserGender());
        userProfile.setUserRating(0); // 초기 평점은 0
        userProfile.setUserInfo(""); // 초기 자기소개는 빈 문자열
        userProfile.setUserImage("http://localhost:8080/images/user.png"); // 초기 이미지 설정
        userProfileRepo.save(userProfile);


        return new SignupRes(user.getId(), signupReq.getUserId(), signupReq.getNickname());
    }


    public FindIdRes findId(FindIdReq findIdReq) throws BaseException {
        List<UserVO> user_list = userRepo.findByEmail(findIdReq.getEmail());
        if (user_list.isEmpty()) {
            throw new BaseException(BaseExceptionStatus.NOT_FOUND_EMAIL);
        }
        return new FindIdRes(user_list.get(0).getId());
    }

    /*
        유저 테이블에서 모든 정보 출력
     */
    public UserVO getUserByUserId(String userId) {
        Optional<UserVO> user = userRepo.findByUserId(userId);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }
        return user.get();
    }

    /*
        유저프로필 테이블에서 정보 출력
     */
    public UserProfileDTO getUserProfileDtoById(int id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return toUserProfileDTO(userProfile);
    }

    private UserProfileDTO toUserProfileDTO(UserProfile userProfile) {
        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserRating(userProfile.getUserRating());
        dto.setUserAge(userProfile.getUserAge());
        dto.setGender(userProfile.getGender());
        dto.setUserInfo(userProfile.getUserInfo());
        dto.setUserImage(userProfile.getUserImage());

        return dto;
    }

    /*
        닉네임, 자기소개, 프사 업데이트 *** 유저이미지부분 고쳐야함
     */
    @Transactional
    public ProfileUpdateDTO updateUserProfile(int id, ProfileUpdateDTO updateDTO) {
        UserVO user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        UserProfile userProfile = userProfileRepo.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("UserProfile not found for user id: " + id));

        boolean isUpdated = false;

        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isEmpty()) {
            user.setNickname(updateDTO.getNickname());
            isUpdated = true; }
        if (updateDTO.getUserInfo() != null && !updateDTO.getUserInfo().isEmpty()) {
            userProfile.setUserInfo(updateDTO.getUserInfo());
            isUpdated = true; }
        if (updateDTO.getUserImage() != null && !updateDTO.getUserImage().isEmpty()) {
            userProfile.setUserImage(updateDTO.getUserImage());
            isUpdated = true; }

        if (isUpdated) {
            userRepo.save(user);
            userProfileRepo.save(userProfile);
        }

        return new ProfileUpdateDTO(user.getNickname(), userProfile.getUserInfo(), userProfile.getUserImage());
    }





    public List<UserReview> getUserReview(String userId) {
        return userReviewRepo.findByUser_UserId(userId);
    }


    public List<BlackList> getUserBlacklist(String userId) {
        return blackListRepo.findByBlockerId(userId);
    }
}