package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.service.BungaeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bungae")
public class BungaeController {

    private final BungaeService bungaeService;

    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);

    // 로그인 체크해야 함
    @GetMapping("/bungaeForm")
    public String bungaeForm() {
        return "bungaeForm";
    }

    @PostMapping("/create_bungae")
    public String createBungae(BungaeDTO bungaeDTO) {
        Bungae bungae = bungaeService.createBungae(bungaeDTO);

        return "redirect:/bungae/" + bungae.getBungaeId();
    }
}
