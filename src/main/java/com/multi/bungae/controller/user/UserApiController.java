package com.multi.bungae.controller.user;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseResponse;
import com.multi.bungae.dto.user.*;
import com.multi.bungae.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public BaseResponse<LoginRes> login(@RequestBody LoginReq loginReq) throws BaseException {
        log.info("Received login request for user: " + loginReq.getUserId());
        LoginRes loginRes = userService.login(loginReq);
        return new BaseResponse<>(loginRes);
    }

    @PostMapping("/check_id")
    @ResponseBody
    public BaseResponse<CheckIdRes> checkId(@RequestBody CheckIdReq checkIdReq) {
        CheckIdRes checkIdRes = userService.checkId(checkIdReq);
        return new BaseResponse<>(checkIdRes);
    }

    @PostMapping("/signup")
    @ResponseBody
    public BaseResponse<SignupRes> signup(@RequestBody SignupReq signupReq) throws BaseException {
        log.info("signupReq: {}", signupReq);
        SignupRes signupRes = userService.signupRes(signupReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping("/find_id")
    @ResponseBody
    public BaseResponse<FindIdRes> findId(FindIdReq findIdReq) throws BaseException {
        FindIdRes findIdRes = userService.findId(findIdReq);
        return new BaseResponse<>(findIdRes);
    }

}
