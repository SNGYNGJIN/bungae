package com.multi.bungae.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
        import com.multi.bungae.service.UserService;
import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseResponse;
import com.multi.bungae.dto.user.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserAPIController {

    @Autowired
    private final UserService userService;

    @PostMapping("check_id")
    @ResponseBody
    public BaseResponse<CheckIdRes> checkId(CheckIdReq checkIdReq) {
        CheckIdRes checkIdRes = userService.checkId(checkIdReq);
        return new BaseResponse<>(checkIdRes);
    }

    @PostMapping("register")
    @ResponseBody
    public BaseResponse<SignupRes> signup(SignupReq signupReq) throws BaseException {
        SignupRes signupRes = userService.signupRes(signupReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping("find_id")
    @ResponseBody
    public BaseResponse<FindIdRes> findId(FindIdReq findIdReq) throws BaseException {
        FindIdRes findIdRes = userService.findId(findIdReq);
        return new BaseResponse<>(findIdRes);
    }

    @PostMapping("login")
    @ResponseBody
    public BaseResponse<LoginRes> login(LoginReq loginReq) throws BaseException {
        LoginRes loginRes = userService.login(loginReq);
        return new BaseResponse<>(loginRes);
    }
}