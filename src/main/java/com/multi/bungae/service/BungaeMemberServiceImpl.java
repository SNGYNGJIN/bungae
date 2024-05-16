package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BungaeMemberServiceImpl implements BungaeMemberService {

    private final BungaeMemberRepository bungaeMemberRepository;

    @Override
    public BungaeMember joinBungae() {
        return null;
    }

    @Override
    public BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer) {

        BungaeMember bungaeMember = new BungaeMember();
        bungaeMember.setBungae(bungae);
        bungaeMember.setUser(user);
        bungaeMember.setOrganizer(isOrganizer);

        return bungaeMemberRepository.save(bungaeMember);
    }

}
