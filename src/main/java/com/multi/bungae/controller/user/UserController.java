package com.multi.bungae.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/")  // 루트 URL을 로그인 페이지로 리디렉션
    public String index() {
        return "redirect:/user/login";
    }
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @GetMapping("/signupSuccess")
    public String signupSuccess() {
        return "user/signupSuccess";
    }

    @GetMapping("/profile")
    public String myPage() {
        return "user/profile";
    }

    @GetMapping("/editProfile")
    public String editProfile() {
        return "user/editProfile";
    }
}