package com.multi.bungae.controller;

import com.multi.bungae.domain.User;
import com.multi.bungae.dto.ResponseDTO;
import com.multi.bungae.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
        import com.multi.bungae.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserAPIController {

    @Autowired
    private final UserService userService;

/*    @PostMapping("check_id")
    @ResponseBody
    public BaseResponse<CheckIdRes> checkId(CheckIdReq checkIdReq) {
        CheckIdRes checkIdRes = userService.checkId(checkIdReq);
        return new BaseResponse<>(checkIdRes);
    }

    @PostMapping("register")
    @ResponseBody
    public BaseResponse<UserDTO> signup(UserDTO signupReq) throws BaseException {
        SignupRes signupRes = userService.signupRes(signupReq);
        return new BaseResponse<>(signupRes);
    }*/
    @PostMapping("api/user")
    public ResponseDTO<Integer> save(@RequestBody UserDTO userDto){
        //System.out.println("@@@@@ UserApiController");
        userDto.setStatus(User.Status.ACTIVE);
        int result = userService.signup(userDto);
        return new ResponseDTO<Integer>(HttpStatus.OK.value(), result);

    }
/*    @PostMapping("find_id")
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
    }*/

}