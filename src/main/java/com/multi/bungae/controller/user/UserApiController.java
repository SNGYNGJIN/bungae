package com.multi.bungae.controller.user;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseResponse;
import com.multi.bungae.domain.BlackList;
import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.user.*;
import com.multi.bungae.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserApiController {

    private final UserService userService;

    /*
        로그인 API
    */
    @PostMapping("/login")
    @ResponseBody
    public BaseResponse<LoginRes> login(@RequestBody LoginReq loginReq) throws BaseException {
        LoginRes loginRes = userService.login(loginReq);
        return new BaseResponse<>(loginRes);
    }

    /*
        중복 아이디 체크 API
     */
    @PostMapping("/check_id")
    @ResponseBody
    public BaseResponse<CheckIdRes> checkId(@RequestBody CheckIdReq checkIdReq) {
        CheckIdRes checkIdRes = userService.checkId(checkIdReq);
        return new BaseResponse<>(checkIdRes);
    }

    /*
        회원가입 API
     */
    @PostMapping("/signup")
    @ResponseBody
    public BaseResponse<SignupRes> signup(@RequestBody SignupReq signupReq) throws BaseException {
        log.info("signupReq: {}", signupReq);
        SignupRes signupRes = userService.signupRes(signupReq);
        return new BaseResponse<>(signupRes);
    }

    /*
        아이디 찾기 API - 구현 X
        (이메일로 아이디를 알려주거나 비밀번호를 재발급 할 수 있게 변경할 듯)
     */
    @PostMapping("/find_id")
    @ResponseBody
    public BaseResponse<FindIdRes> findId(FindIdReq findIdReq) throws BaseException {
        FindIdRes findIdRes = userService.findId(findIdReq);
        return new BaseResponse<>(findIdRes);
    }

    /*
        userID를 통해 user 테이블의 모든 정보를 가져오는 API
     */
    @GetMapping("/info/{userId}")
    @ResponseBody
    public BaseResponse<UserVO> getUserInfo(@PathVariable String userId) throws BaseException{
        UserVO user = userService.getUserByUserId(userId);
        return new BaseResponse<>(user);
    }

    /*
        id를 통해 user_profile 테이블의 모든 정보를 가져오는 API
     */
    @GetMapping("/info/profile/{id}")
    @ResponseBody
    public BaseResponse<UserProfileDTO> getUserProfileDtoById(@PathVariable int id) throws BaseException{
        UserProfileDTO user = userService.getUserProfileDtoById(id);
        return new BaseResponse<>(user);
    }



    /*
        userID를 통해 user_review 테이블의 모든 리뷰를 가져오는 API
    */
    @GetMapping("/info/review/{userId}")
    @ResponseBody
    public BaseResponse<List<UserReview>> getUserReviews(@PathVariable String userId) throws BaseException{
        List<UserReview> reviews = userService.getUserReview(userId);
        return new BaseResponse<>(reviews);
    }

    /*
        userID를 통해 blacklist 테이블의 모든 블랙리스트 항목을 가져오는 API
    */
    @GetMapping("/info/black/{userId}")
    @ResponseBody
    public BaseResponse<List<BlackList>> getUserBlacklists(@PathVariable String userId) throws BaseException{
        List<BlackList> blackLists = userService.getUserBlacklist(userId);
        return new BaseResponse<>(blackLists);
    }

}
