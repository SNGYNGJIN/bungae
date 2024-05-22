package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.BungaeRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
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
        Optional<BungaeMember> duplicatedMember = bungaeMemberRepository.findByUser(user);
        if (duplicatedMember.isPresent()) {
            throw new IllegalStateException("해당 유저가 이미 다른 번개 모임에 참여하고 있습니다.");
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
    public BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer) {

        BungaeMember bungaeMember = new BungaeMember();
        bungaeMember.setBungae(bungae);
        bungaeMember.setUser(user);
        bungaeMember.setOrganizer(isOrganizer);

        return bungaeMemberRepository.save(bungaeMember);
    }

}
