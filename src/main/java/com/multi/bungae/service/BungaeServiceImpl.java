package com.multi.bungae.service;

import com.multi.bungae.controller.BungaeController;
import com.multi.bungae.domain.*;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.LocationDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.BungaeRepository;
import com.multi.bungae.utils.BungaeSpecification;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.domain.Specification;
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
        bungaeRepository.save(bungae);
        bungaeMemberService.createBungaeMember(bungae, user, true);
        chatService.createChat(bungae.getBungaeId(), user.getUserId());
        return bungae;
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeList() {
        List<Bungae> bungaeList = bungaeRepository.findAll();
        return getBungaeDTOList(bungaeList);
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeListOfStartTime() {
        List<Bungae> bungaeList = bungaeRepository.findAllByOrderByBungaeStartTimeAsc();
        return getBungaeDTOList(bungaeList);
    }

    @Override
    @Transactional
    public List<BungaeDTO> bungaeListOfCreateTime() {
        List<Bungae> bungaeList = bungaeRepository.findAllByOrderByBungaeCreateTimeDesc();
        return getBungaeDTOList(bungaeList);
    }

    @Override
    @Transactional
    public List<BungaeDTO> findBungaeNearby(Point userLocation, double radius) {
        List<Bungae> bungaeList = bungaeRepository.findBungaeNearby(userLocation, radius);
        return bungaeList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Bungae updateBungae(Long bungaeId, BungaeDTO bungaeDTO) {
        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);

        if (bungaeOptional.isPresent()) {
            Bungae bungae = bungaeOptional.get();
            Bungae updatedBungae = updateBungaeData(bungae, bungaeDTO);

            return bungaeRepository.save(updatedBungae);

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

    @Override
    @Transactional
    public void updateBungaeStatus() {
        LocalDateTime now = LocalDateTime.now();

        bungaeRepository.updateStatusToInProgress(BungaeStatus.SCHEDULED, BungaeStatus.IN_PROGRESS, now);

        bungaeRepository.updateStatusToEnded(BungaeStatus.IN_PROGRESS, BungaeStatus.ENDED, now.minusHours(1));
    }

    @Override
    @Transactional
    public List<BungaeDTO> search(String keyword) {
        Specification<Bungae> spec = Specification
                .where(BungaeSpecification.containsKeywordInNameDescriptionLocation(keyword))
                .and(BungaeSpecification.hasStatus(BungaeStatus.SCHEDULED));
        List<Bungae> bungaeList = bungaeRepository.findAll(spec);
        return getBungaeDTOList(bungaeList);
    }

    private Bungae updateBungaeData(Bungae bungae, BungaeDTO bungaeDTO) {
        LocalDateTime createTime = LocalDateTime.now();

        bungae.setBungaeType(bungaeDTO.getBungaeType());
        bungae.setBungaeName(bungaeDTO.getBungaeName());
        bungae.setBungaeDescription(bungaeDTO.getBungaeDescription());
        bungae.setBungaeLocation(LocationDTO.toEntity(bungaeDTO.getBungaeLocation()));
        bungae.setBungaeImagePath(bungaeDTO.getBungaeImagePath());
        bungae.setBungaeMaxMember(bungaeDTO.getBungaeMaxMember());
        bungae.setBungaeCreateTime(createTime);
        bungae.setBungaeStartTime(bungaeDTO.getBungaeStartTime());
        bungae.setBungaeMinAge(bungaeDTO.getBungaeMinAge());
        bungae.setBungaeMaxAge(bungaeDTO.getBungaeMaxAge());
        return bungae;
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

    private List<BungaeDTO> getBungaeDTOList(List<Bungae> bungaeList) {
        return bungaeList.stream()
                .filter(bungae -> bungae.getBungaeStatus() != BungaeStatus.ENDED)
                .map(bungae -> {
                    BungaeDTO bungaeDTO = convertToDTO(bungae);
                    int currentMemberCount = bungaeMemberRepository.countByBungae_BungaeId(bungae.getBungaeId());
                    bungaeDTO.setCurrentMemberCount(currentMemberCount);
                    return bungaeDTO;
                })
                .collect(Collectors.toList());
    }
}
