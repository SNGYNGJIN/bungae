package com.multi.bungae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BungaeScheduler {

    private final BungaeService bungaeService;

    @Scheduled(fixedRate = 600000) // 1분마다 실행
    public void scheduleFixedRateTask() {
        bungaeService.updateBungaeStatus();
    }
}
