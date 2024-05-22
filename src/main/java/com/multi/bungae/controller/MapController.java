package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.repository.BungaeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MapController {


    @GetMapping("/map")
    public String getMap() {
        return "map";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/bungae_create")
    public String bungae_create() {
        return "bungae_create";
    }

    @GetMapping("/bungae_detail")
    public String bungae_detail() {
        return "bungae_detail";
    }

    @GetMapping("/bungae_ing2")
    public String bungae_ing() {
        return "bungae_ing";
    }

    @GetMapping("/bungae_list")
    public String bungae_list() {
        return "bungae_list";
    }

    @GetMapping("/chatting")
    public String chatting() {
        return "chatting";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/profile_edit")
    public String profile_edit() {
        return "profile_edit";
    }

    @GetMapping("/testmap")
    public String testmap() {
        return "testmap";
    }


}
