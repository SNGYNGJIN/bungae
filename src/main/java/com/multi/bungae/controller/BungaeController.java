package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.service.BungaeService;
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

    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);

    /**
     * 로그인 체크해야 함
     */
    @GetMapping("/bungaeForm")
    public String bungaeForm() {
        return "bungaeForm";
    }

    @PostMapping("/create_bungae")
    public String createBungae(BungaeDTO bungaeDTO) {
        Bungae bungae = bungaeService.createBungae(bungaeDTO);

        return "redirect:/index.html";
    }

    @GetMapping("/bungaeList")
    public List<Bungae> bungaeList() {
        return null;
    }

    @PostMapping("/findBungae")
    public Bungae findBungae() {
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
