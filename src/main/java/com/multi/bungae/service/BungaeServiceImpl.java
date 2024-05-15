package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeStatus;
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
import java.util.stream.Collectors;

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
    @Transactional
    public Bungae createBungae(BungaeDTO bungaeDTO) {

        Bungae bungae = new Bungae(
                null,  // 생성 시 자동으로 할당
                bungaeDTO.getBungaeType(),
                bungaeDTO.getBungaeName(),
                bungaeDTO.getBungaeLocation(),
                bungaeDTO.getBungaeImageName(),
                bungaeDTO.getBungaeImagePath(),
                bungaeDTO.getBungaeMaxMember(),
                bungaeDTO.getBungaeCreateTime(),
                bungaeDTO.getBungaeStartTime(),
                bungaeDTO.getBungaeMinAge(),
                bungaeDTO.getBungaeMaxAge(),
                BungaeStatus.ACTIVE
        );

        return bungaeRepository.save(bungae);
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeList() {
        List<Bungae> bungaeList = bungaeRepository.findAll();
        return bungaeList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BungaeDTO> findBungaeNearby(Point userLocation, double radius) {
        List<Bungae> bungaeList = bungaeRepository.findBungaeNearby(userLocation, radius);
        return bungaeList.stream().map(this::convertToDTO).collect(Collectors.toList());
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

    /**
     * 실제 db에서 삭제
     */
    @Override
    @Transactional
    public void cancelBungae(Long bungaeId) {

        if (!bungaeRepository.existsById(bungaeId)) {
            throw new RuntimeException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }
        bungaeRepository.deleteById(bungaeId);
    }

    /**
     * 상태코드를 변경해서 삭제된 것 처럼 처리
     */
    @Override
    @Transactional
    public Bungae cancelBungae2(Long bungaeId) {

        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);

        if (bungaeOptional.isPresent()) {
            Bungae bungae = bungaeOptional.get();

            bungae.setBungaeStatus(BungaeStatus.CANCELLED);
            return bungaeRepository.save(bungae);

        } else {
            throw new RuntimeException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }
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

    private BungaeDTO convertToDTO(Bungae bungae) {
        BungaeDTO dto = new BungaeDTO();
        dto.setBungaeId(bungae.getBungaeId());
        dto.setBungaeType(bungae.getBungaeType());
        dto.setBungaeName(bungae.getBungaeName());
        dto.setBungaeLocation(bungae.getBungaeLocation());
        dto.setBungaeImageName(bungae.getBungaeImageName());
        dto.setBungaeImagePath(bungae.getBungaeImagePath());
        dto.setBungaeMaxMember(bungae.getBungaeMaxMember());
        dto.setBungaeCreateTime(bungae.getBungaeCreateTime());
        dto.setBungaeStartTime(bungae.getBungaeStartTime());
        dto.setBungaeMinAge(bungae.getBungaeMinAge());
        dto.setBungaeMaxAge(bungae.getBungaeMaxAge());
        return dto;
    }

    @Override
    public List<Bungae> findBungaeNearby(Point userLocation, double radius) {
        return bungaeRepository.findBungaeNearby(userLocation, radius);
    }
}
