package com.multi.bungae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class UserStatusScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 4 * 60 * 1000) // 4분마다 실행
    public void checkUserActivity() {
        System.out.println("Checking user activity at: " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        userService.checkAndUpdateUserStatus();
    }
}