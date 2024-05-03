package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.dto.BungaeDTO;
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
    public Bungae createBungae(BungaeDTO bungaeDTO) {

        Bungae bungae = new Bungae(
                null,  // 생성 시 자동으로 할당
                bungaeDTO.getBungaeType(),
                //null,  // bungaeLocation: 나중에
                bungaeDTO.getBungaeImageName(),
                bungaeDTO.getBungaeImagePath(),
                bungaeDTO.getBungaeMaxMember(),
                bungaeDTO.getBungaeCreateTime(),
                bungaeDTO.getBungaeStartTime(),
                bungaeDTO.getBungaeMinAge(),
                bungaeDTO.getBungaeMaxAge(),
                bungaeDTO.getBungaeStatus()
        );

        return bungaeRepository.save(bungae);
    }
}
