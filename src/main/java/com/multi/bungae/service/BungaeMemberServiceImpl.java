package com.multi.bungae.service;

import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.repository.BungaeMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BungaeMemberServiceImpl implements BungaeMemberService {

    private final BungaeMemberRepository bungaeMemberRepository;

    @Override
    public BungaeMember joinBungae() {
        return null;
    }
}
