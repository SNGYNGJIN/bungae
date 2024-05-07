package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.BungaeMemberDTO;
import com.multi.bungae.service.BungaeMemberService;
import com.multi.bungae.service.BungaeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bungae")
public class BungaeController {

    private final BungaeService bungaeService;
    private final BungaeMemberService bungaeMemberService;

    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);

    @GetMapping("/bungaeForm")
    public String bungaeForm() {
        return "bungaeForm";
    }

    @PostMapping("/create_bungae")
    public String createBungae(BungaeDTO bungaeDTO, HttpSession session) {

//        UserVO user = (UserVO) session.getAttribute("loggedInUser"); // 로그인된 유저 연결
//        Bungae bungae = bungaeService.createBungae(bungaeDTO, user);
        Bungae bungae = bungaeService.createBungae(bungaeDTO);
        logger.info("bungae: " + bungae);

        return "redirect:/index.html";
    }

    @GetMapping(value = "/bungaeList", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<Bungae> bungaeList() {
        List<Bungae> list = bungaeService.bungaeList();
        return list;
    }

    @GetMapping("/find/location")
    public Bungae findBungaeByLocation(@RequestParam("location") String location) {
        return null;
    }

    @GetMapping("/find/type")
    public Bungae findBungaeByType(@RequestParam("type") String bungaeType) {
        return null;
    }

    @GetMapping("/find/age")
    public Bungae findBungaeByAge(@RequestParam("age") int age) {
        return null;
    }

    @PostMapping("/editBungae/{bungaeId}")
    public Bungae editBungae(/*주최자일때만*/@PathVariable Long bungaeId) {
        return null;
    }

    @DeleteMapping("/{bungaeId}")
    public void cancelBungae(/*주최자일때만*/@PathVariable Long bungaeID) {

    }
}
