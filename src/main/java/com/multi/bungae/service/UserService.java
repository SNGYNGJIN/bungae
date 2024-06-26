package com.multi.bungae.service;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseExceptionStatus;
import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.*;

import com.multi.bungae.dto.OfflineDTO;
import com.multi.bungae.dto.user.*;
import com.multi.bungae.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.multi.bungae.config.BaseExceptionStatus.INVALID_PASSWORD;
import static com.multi.bungae.utils.ValidationRegex.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);

    private final UserRepository userRepo;
    private final OfflineStateRepository offlineStateRepo;
    private final UserProfileRepository userProfileRepo;
    private final UserReviewRepository userReviewRepo;
    private final BlackListRepository blackListRepo;
    //private final ImageRepository imageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return null;
    }


    public CheckIdRes checkId(CheckIdReq checkIdReq) {
        return new CheckIdRes(!userRepo.existsUserByUserId(checkIdReq.getUserId()));
    }

    public LoginRes login(@RequestBody LoginReq loginReq, HttpSession session) throws BaseException {
        Optional<UserVO> userOpt = userRepo.findByUserId(loginReq.getUserId());
        if (userOpt.isPresent() && passwordEncoder.matches(loginReq.getPasswd(), userOpt.get().getPassword())) {
            session.setAttribute("loggedInUserId", userOpt.get().getUserId());
            session.setAttribute("loggedInId", userOpt.get().getId());

            return new LoginRes("access_token_OK", "refresh_token_OK"); // Replace with actual token generation
        } else {
            throw new BaseException(BaseExceptionStatus.LOGIN_FAILED);
        }
    }

    @Transactional
    public SignupRes signupRes(@RequestBody SignupReq signupReq) throws BaseException {

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
            System.out.println("userId값이 " + signupReq.getUserId() + "입니다.");
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
        if (signupReq.getGender() == null || !isRegexGender(signupReq.getGender().name())) {
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
                .bungaeMembers(new HashSet<>())
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

        // 유저 상태 오프라인으로 설정
        OfflineState offlineState = new OfflineState();
        offlineState.setUser(user);
        offlineState.setState(OfflineState.State.OFFLINE);
        user.setState(offlineState);

        return new SignupRes(user.getId(), signupReq.getUserId(), signupReq.getNickname());
    }

    /*
        아이디 찾기
     */
    public FindIdRes findId(FindIdReq findIdReq) throws BaseException {
        List<UserVO> user_list = userRepo.findByEmailAndUserBirthAndUsername(findIdReq.getEmail(), findIdReq.getBirth(), findIdReq.getName());
        if (user_list.isEmpty()) {
            throw new BaseException(BaseExceptionStatus.NOT_FOUND_EMAIL);
        }
        return new FindIdRes(user_list.get(0).getUserId());
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
        닉네임, 자기소개, 프사 업데이트
     */
    public UserProfile getUserProfileByUserId(String userId) {
        return userProfileRepo.findByUser_UserId(userId);
    }

    public UserProfileDTO saveUserProfile(UserProfile userProfile) {
        UserProfileDTO dto = new UserProfileDTO();
        UserProfile up = userProfileRepo.save(userProfile);

        dto.setUserInfo(up.getUserInfo());
        dto.setUserImage(up.getUserImage());

        return dto;
    }

    /*
        사용자의 마지막 활동이 5분이 넘었으면 OFFLINE으로 변환
     */
    @Transactional
    public void checkAndUpdateUserStatus() {
        List<UserVO> users = userRepo.findAll();
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        OfflineDTO dto = new OfflineDTO();
        dto.setState("down");

        for (UserVO user : users) {
            if (user.getState().getLastActive() != null && user.getState().getLastActive().isBefore(now.minusMinutes(5))) {
                updateOfflineState(user.getUserId(), dto);
            }
        }
    }

    /*
        사용자의 인터넷 상태를 감지하고 온오프 변환하기
     */
    @Transactional
    public void updateOfflineState(String userId, OfflineDTO state) {
        OfflineState.State newState;

        if ("up".equals(state.getState())) {
            newState = OfflineState.State.ONLINE;
        } else {
            newState = OfflineState.State.OFFLINE;
        }

        UserVO user = userRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OfflineState offlineState = user.getState();


        if (offlineState.getState() != newState) {

            offlineState.setLastActive(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            offlineState.setState(newState);
            offlineStateRepo.save(offlineState);
            System.out.println("Updated offline state for user " + userId + ": " + newState);
            userRepo.save(user);
        } else {
            offlineState.setLastActive(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            offlineStateRepo.save(offlineState);
            System.out.println("State is already " + newState);
        }
    }


    public List<BlackList> getUserBlacklist(String userId) {
        return blackListRepo.findByBlockerId(userId);
    }
}