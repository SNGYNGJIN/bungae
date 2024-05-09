package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.repository.BungaeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BungaeServiceImpl implements BungaeService {

    private final BungaeRepository bungaeRepository;
    private final BungaeMemberService bungaeMemberService;

//    @Override
//    public Bungae createBungae(BungaeDTO bungaeDTO, UserVO user) {
//
//        LocalDateTime createTime = LocalDateTime.now();
//
//        Bungae bungae = new Bungae(
//                null,  // 생성 시 자동으로 할당
//                bungaeDTO.getBungaeType(),
//                bungaeDTO.getBungaeName(),
//                bungaeDTO.getBungaeLocation(),
//                bungaeDTO.getBungaeImageName(),
//                bungaeDTO.getBungaeImagePath(),
//                bungaeDTO.getBungaeMaxMember(),
//                createTime,
//                bungaeDTO.getBungaeStartTime(),
//                bungaeDTO.getBungaeMinAge(),
//                bungaeDTO.getBungaeMaxAge(),
//                bungaeDTO.getBungaeStatus()
//        );
//
//        bungaeMemberService.createBungaeMember(bungae, user, true);
//
//        return bungaeRepository.save(bungae);
//    }

    @Override
    public Bungae createBungae(BungaeDTO bungaeDTO) {

        LocalDateTime createTime = LocalDateTime.now();

        Bungae bungae = new Bungae(
                null,  // 생성 시 자동으로 할당
                bungaeDTO.getBungaeType(),
                bungaeDTO.getBungaeName(),
                bungaeDTO.getBungaeLocation(),
                bungaeDTO.getBungaeImageName(),
                bungaeDTO.getBungaeImagePath(),
                bungaeDTO.getBungaeMaxMember(),
                createTime,
                bungaeDTO.getBungaeStartTime(),
                bungaeDTO.getBungaeMinAge(),
                bungaeDTO.getBungaeMaxAge(),
                bungaeDTO.getBungaeStatus()
        );

        return bungaeRepository.save(bungae);
    }

    @Override
    public List<Bungae> bungaeList() {
        return bungaeRepository.findAll();
    }

    @Override
    public List<Bungae> findBungaeNearby(Point userLocation, double radius) {
        return bungaeRepository.findBungaeNearby(userLocation, radius);
    }
}
