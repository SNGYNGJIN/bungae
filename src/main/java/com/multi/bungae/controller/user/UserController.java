package com.multi.bungae.controller.user;

import com.multi.bungae.domain.UserVO;
import com.multi.bungae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")  // 루트 URL을 로그인 페이지로 리디렉션
    public String index() {
        return "redirect:/user/login";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
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
    @GetMapping("/profile/{userId}")
    public String myPage(Model model, @PathVariable String userId) {
        UserVO user = userService.getUserByUserId(userId);
        model.addAttribute("user",user);
        return "user/profile";
    }
    @GetMapping("/editProfile")
    public String editProfile() {
        return "user/editProfile";
    }
}