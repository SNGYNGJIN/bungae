package com.multi.bungae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserStatusScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void checkUserActivity() {
        userService.checkAndUpdateUserStatus();
    }
}