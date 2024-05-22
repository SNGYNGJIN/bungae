package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.service.BungaeMemberService;
import com.multi.bungae.service.BungaeMemberServiceImpl;
import com.multi.bungae.service.BungaeService;
import com.multi.bungae.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BungaeMemberController {

    private final BungaeMemberService bungaeMemberService;
    private final BungaeService bungaeService;
    private final UserService userService;

    @PostMapping("/{bungaeId}/join")
    public ResponseEntity<String> joinBungae(@PathVariable Long bungaeId, HttpSession session) {
        String userId = (String) session.getAttribute("loggedInUserId");
        Bungae bungae = bungaeService.getBungaeById(bungaeId);
        if (bungae.getBungaeStatus() == BungaeStatus.ENDED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 모임이 이미 종료되었습니다.");
        }
        try {
            if (bungaeMemberService.isOrganizerTrue(bungaeId, userId)) {
                return ResponseEntity.ok("주최자");
            } else if (bungaeMemberService.isOrganizerFalse(bungaeId, userId)){
                return ResponseEntity.ok("참여자");
            }
            BungaeMember bungaeMember = bungaeMemberService.joinBungae(bungaeId, userId);
            return ResponseEntity.ok("새로운 참여자");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/bungae_ing")
    public String bungaeInAttendance(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loggedInUserId");
        UserVO user = userService.getUserByUserId(userId);
        Bungae bungae = bungaeMemberService.findBungaeById(user.getId());
        model.addAttribute("bungae", bungae);
        return "bungae_ing";
    }
}
