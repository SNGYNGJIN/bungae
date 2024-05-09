package com.multi.bungae.controller;

import com.multi.bungae.config.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.multi.bungae.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //private final UserService userService;
    //private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 메인 페이지(로그인 페이지)
    @GetMapping("/login")
    public String home() {
        return "/user/login";
    }

    // 회원가입 페이지
    @GetMapping("/signupForm")
    public String dispSignup() {
        return "/user/signup";
    }

//    // 회원가입 처리
//    @PostMapping("/signup")
//    public String execSignup(SignupReq signupReq, RedirectAttributes redirectAttributes) {
//        try {
//            SignupRes signupRes = userService.signupRes(signupReq);
//            redirectAttributes.addFlashAttribute("successMessage", "회원가입에 성공하였습니다.");
//            return "redirect:/user/login";
//        } catch (BaseException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
//            return "redirect:/user/signup";  // 실패시 다시 회원가입 페이지로 리디렉트
//        }
//    }

    // 회원가입 결과 페이지
    @GetMapping("/signup/result")
    public String dispLoginResult() {
        return "signupSuccess";
    }

    // 접근 거부 페이지
    @GetMapping("/denied")
    public String dispDenied() {
        return "denied";
    }

    // 내 정보 페이지
    @GetMapping("/info")
    public String dispMyInfo() {
        return "myinfo";
    }
}
