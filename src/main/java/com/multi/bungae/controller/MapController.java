package com.multi.bungae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @Value("${naver.map.client.id}")
    private String clientId;

    @Value("${naver.map.client.secret}")
    private String clientSecret;

    @GetMapping("/map")
    public String getMap(Model model) {
        model.addAttribute("clientId", clientId);
        return "map";
    }
}
