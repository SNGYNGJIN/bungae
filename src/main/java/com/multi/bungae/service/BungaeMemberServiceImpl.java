package com.multi.bungae.service;

import com.multi.bungae.domain.*;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.BungaeRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BungaeMemberServiceImpl implements BungaeMemberService {

    private final BungaeMemberRepository bungaeMemberRepository;
    private final BungaeRepository bungaeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BungaeMember joinBungae(Long bungaeId, String userId) {
        Optional<Bungae> bungaeOptional = bungaeRepository.findById(bungaeId);
        Optional<UserVO> userOptional = userRepository.findByUserId(userId);

        if (!bungaeOptional.isPresent()) {
            throw new IllegalArgumentException("해당 id를 가진 번개모임이 없음: " + bungaeId);
        }

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("해당 id를 가진 유저가 없음: " + userId);
        }

        Bungae bungae = bungaeOptional.get();
        UserVO user = userOptional.get();

        // 중복 가입 체크
        if (!canJoinOrHostBungae(user)) {
            throw new IllegalStateException("참가 중인 모임이 있으면 다른 모임에 참여할 수 없습니다.");
        }
        // 연령대 체크
        if (user.getProfile().getUserAge() < bungae.getBungaeMinAge() || user.getProfile().getUserAge() > bungae.getBungaeMaxAge()) {
            throw new IllegalStateException("연령대에 맞지 않는 번개 모임입니다.");
        }
        // 인원수 체크
        if (bungae.getBungaeMaxMember() <= bungaeMemberRepository.countByBungae_BungaeId(bungaeId)) {
            throw new IllegalStateException("수용 인원 초과된 번개 모임입니다.");
        }

        BungaeMember bungaeMember = new BungaeMember();
        bungaeMember.setBungae(bungae);
        bungaeMember.setUser(user);
        bungaeMember.setOrganizer(false);

        return bungaeMemberRepository.save(bungaeMember);
    }

    @Override
    @Transactional
    public List<Bungae> findBungaeByUserId(int userId) {
        return bungaeMemberRepository.findBungaeByUserId(userId);
    }

    @Override
    @Transactional
    public Bungae findBungaeById(int userId) {
        return bungaeMemberRepository.findBungaeById(userId);
    }

    @Override
    @Transactional
    public boolean canJoinOrHostBungae(UserVO user) {
        List<BungaeMember> activeBungaeList = bungaeMemberRepository.findByUserAndBungae_BungaeStatus(user, BungaeStatus.SCHEDULED);
        activeBungaeList.addAll(bungaeMemberRepository.findByUserAndBungae_BungaeStatus(user, BungaeStatus.IN_PROGRESS));

        return activeBungaeList.isEmpty();
    }

    @Override
    @Transactional
    public Bungae findActiveBungaeByUserId(int userId) {
        return bungaeMemberRepository.findByUser_IdAndBungae_BungaeStatusNot(userId, BungaeStatus.ENDED)
                .map(BungaeMember::getBungae)
                .orElse(null);
    }

    @Override
    @Transactional
    public int countByBungae_BungaeId(Long bungaeId) {
        return bungaeMemberRepository.countByBungae_BungaeId(bungaeId);
    }

    @Override
    @Transactional
    public boolean isOrganizerTrue(Long bungaeId, String userId) {
        UserVO user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepository.findById(bungaeId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        return bungaeMemberRepository.existsByBungaeAndUserAndIsOrganizerTrue(bungae, user);
    }

    @Override
    @Transactional
    public boolean isOrganizerFalse(Long bungaeId, String userId) {
        UserVO user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepository.findById(bungaeId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        return bungaeMemberRepository.existsByBungaeAndUserAndIsOrganizerFalse(bungae, user);
    }

    @Override
    @Transactional
    public BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer) {

        BungaeMember bungaeMember = new BungaeMember();
        bungaeMember.setBungae(bungae);
        bungaeMember.setUser(user);
        bungaeMember.setOrganizer(isOrganizer);

        return bungaeMemberRepository.save(bungaeMember);
    }

}
