package com.multi.bungae.controller;

import com.multi.bungae.service.BungaeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bungae")
public class BungaeController {

    private final BungaeService bungaeService;

    @GetMapping("/create_bungae")
    public String createBungae() {
        return "createBungaeForm";
    }
}
