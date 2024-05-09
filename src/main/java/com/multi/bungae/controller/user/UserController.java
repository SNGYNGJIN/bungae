package com.multi.bungae.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/")  // 루트 URL을 처리하는 메소드
    public String index() {
        return "/user/login";  // 루트 URL 접속 시 로그인 페이지로 리다이렉션
    }
    @GetMapping("/logins")
    public String login() {
        return "user/login";  // 실제 로그인 페이지 반환
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

}