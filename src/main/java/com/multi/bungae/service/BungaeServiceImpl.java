package com.multi.bungae.service;

import com.multi.bungae.controller.BungaeController;
import com.multi.bungae.domain.*;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.LocationDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.BungaeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class BungaeServiceImpl implements BungaeService {

    private final BungaeRepository bungaeRepository;
    private final BungaeMemberRepository bungaeMemberRepository;
    private final BungaeMemberService bungaeMemberService;
    private final ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);


    @Override
    @Transactional
    public Bungae getBungaeById(Long bungaeId) {
        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);

        if (bungaeOptional.isPresent()) {
            return bungaeOptional.get();
        } else {
            throw new RuntimeException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }
    }

    @Override
    @Transactional
    public Bungae createBungae(BungaeDTO bungaeDTO, UserVO user) {
/*Hyeyeon
        logger.info("Creating Bungae with type: {}, name: {}", bungaeDTO.getBungaeType(), bungaeDTO.getBungaeName());
        if (bungaeDTO == null || user == null) { // user의 id 값 받아온 것임 ex.1
            logger.error("BungaeDTO or UserVO is null");
            throw new IllegalArgumentException("BungaeDTO and UserVO must not be null");
        }

*/
        if (!bungaeMemberService.canJoinOrHostBungae(user)) {
            throw new IllegalStateException("참가 중인 모임이 있으면 새로운 모임을 주최할 수 없습니다.");
        }

        LocalDateTime createTime = LocalDateTime.now();

        Bungae bungae = new Bungae(
                null,
                bungaeDTO.getBungaeType(),
                bungaeDTO.getBungaeName(),
                bungaeDTO.getBungaeDescription(),
                LocationDTO.toEntity(bungaeDTO.getBungaeLocation()),
                bungaeDTO.getBungaeImagePath(),
                bungaeDTO.getBungaeMaxMember(),
                createTime,
                bungaeDTO.getBungaeStartTime(),
                bungaeDTO.getBungaeMinAge(),
                bungaeDTO.getBungaeMaxAge(),
                BungaeStatus.SCHEDULED,
                null
        );
/* Hyeyeon
        bungae = bungaeRepository.save(bungae);
        logger.info("Bungae object created successfully with ID: {}", bungae.getBungaeId());

        // 채팅방 생성
        chatService.createChat(bungae.getBungaeId(), user.getUserId()); // 여기서 user의 userId 넣기

        // BungaeMember 생성
        bungaeMemberService.createBungaeMember(bungae, user, true);

        return bungae;
*/
        bungaeRepository.save(bungae);
        bungaeMemberService.createBungaeMember(bungae, user, true);
        chatService.createChat(bungae.getBungaeId(), user.getUserId());
        return bungae;

    }


    @Override
    @Transactional
    public List<BungaeDTO> bungaeList() {
        List<Bungae> bungaeList = bungaeRepository.findAll();
        return bungaeList.stream()
                .filter(bungae -> bungae.getBungaeStatus() != BungaeStatus.ENDED)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeListOfStartTime() {
        List<Bungae> bungaeList = bungaeRepository.findAllByOrderByBungaeStartTimeAsc();
        return bungaeList.stream()
                .filter(bungae -> bungae.getBungaeStatus() != BungaeStatus.ENDED)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeListOfCreateTime() {
        List<Bungae> bungaeList = bungaeRepository.findAllByOrderByBungaeCreateTimeDesc();
        return bungaeList.stream()
                .filter(bungae -> bungae.getBungaeStatus() != BungaeStatus.ENDED)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BungaeDTO> findBungaeNearby(Point userLocation, double radius) {
        List<Bungae> bungaeList = bungaeRepository.findBungaeNearby(userLocation, radius);
        return bungaeList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Bungae editBungae(Long bungaeId, BungaeDTO bungaeDTO, UserVO user) {
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
    public void cancelBungae(Long bungaeId, UserVO user) {

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
    public Bungae cancelBungae2(Long bungaeId, UserVO user) {

        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);

        if (bungaeOptional.isPresent()) {
            Bungae bungae = bungaeOptional.get();

            bungae.setBungaeStatus(BungaeStatus.ENDED);
            return bungaeRepository.save(bungae);

        } else {
            throw new RuntimeException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }
    }

    private void updateBungaeData(Bungae bungae, BungaeDTO bungaeDTO) {
        bungae.setBungaeType(bungaeDTO.getBungaeType());
        bungae.setBungaeName(bungaeDTO.getBungaeName());
        bungae.setBungaeDescription(bungaeDTO.getBungaeDescription());
        bungae.setBungaeLocation(LocationDTO.toEntity(bungaeDTO.getBungaeLocation()));
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
        dto.setBungaeDescription(bungae.getBungaeDescription());
        dto.setBungaeLocation(LocationDTO.fromEntity(bungae.getBungaeLocation()));
        dto.setBungaeImagePath(bungae.getBungaeImagePath());
        dto.setBungaeMaxMember(bungae.getBungaeMaxMember());
        dto.setBungaeCreateTime(bungae.getBungaeCreateTime());
        dto.setBungaeStartTime(bungae.getBungaeStartTime());
        dto.setBungaeMinAge(bungae.getBungaeMinAge());
        dto.setBungaeMaxAge(bungae.getBungaeMaxAge());
        return dto;
    }

    public void updateBungaeStatus() {
        List<Bungae> bungaeList = bungaeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Bungae bungae : bungaeList) {
            if (bungae.getBungaeStatus() == BungaeStatus.SCHEDULED && bungae.getBungaeStartTime().isBefore(now)) {
                bungae.setBungaeStatus(BungaeStatus.IN_PROGRESS);
            } else if (bungae.getBungaeStatus() == BungaeStatus.IN_PROGRESS && bungae.getBungaeStartTime().plusHours(1).isBefore(now)) {
                bungae.setBungaeStatus(BungaeStatus.ENDED);
            }
            bungaeRepository.save(bungae);
        }
    }
}
