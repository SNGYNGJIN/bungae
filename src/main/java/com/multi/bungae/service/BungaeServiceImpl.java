package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.repository.BungaeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BungaeServiceImpl implements BungaeService {

    private final BungaeRepository bungaeRepository;

    @Override
    public Bungae createBungae() {
        Bungae bungae = new Bungae();
        bungae.setBungaeId(1L); // 임의의 ID 값 설정
        bungae.setBungaeType(1); // 임의의 타입 값 설정
        bungae.setBungaeImageName("exampleImageName"); // 임의의 이미지 이름 설정
        bungae.setBungaeImagePath("exampleImagePath"); // 임의의 이미지 경로 설정
        bungae.setBungaeMaxMember(10); // 임의의 최대 멤버 수 설정
        bungae.setBungaeCreateTime(LocalDateTime.now()); // 현재 시간으로 설정
        bungae.setBungaeStartTime(LocalDateTime.now()); // 현재 시간으로 설정
        bungae.setBungaeMinAge((byte) 20); // 임의의 최소 연령 설정
        bungae.setBungaeMaxAge((byte) 30); // 임의의 최대 연령 설정
        bungae.setBungaeStatus((byte) 1); // 임의의 상태 설정

        return bungaeRepository.save(bungae);
    }
}
