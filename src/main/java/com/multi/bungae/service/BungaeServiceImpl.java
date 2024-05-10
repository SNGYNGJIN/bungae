package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.repository.BungaeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional
    public Bungae editBungae(Long bungaeId, BungaeDTO bungaeDTO) {
        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);

        if (bungaeOptional.isPresent()) {
            Bungae bungae = bungaeOptional.get();
            updateBungaeData(bungae, bungaeDTO);

            return bungaeRepository.save(bungae);

        } else {
            throw new RuntimeException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }
    }

    public void cancelBungae(Long bungaeId) {
        
    }

    private void updateBungaeData(Bungae bungae, BungaeDTO bungaeDTO) {
        bungae.setBungaeType(bungaeDTO.getBungaeType());
        bungae.setBungaeName(bungaeDTO.getBungaeName());
        bungae.setBungaeLocation(bungaeDTO.getBungaeLocation());
        bungae.setBungaeImageName(bungaeDTO.getBungaeImageName());
        bungae.setBungaeImagePath(bungae.getBungaeImagePath());
        bungae.setBungaeMaxMember(bungaeDTO.getBungaeMaxMember());
        bungae.setBungaeCreateTime(bungaeDTO.getBungaeCreateTime());
        bungae.setBungaeStartTime(bungaeDTO.getBungaeStartTime());
        bungae.setBungaeMinAge(bungaeDTO.getBungaeMinAge());
        bungae.setBungaeMaxAge(bungaeDTO.getBungaeMaxAge());
    }
}
