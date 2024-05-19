package com.multi.bungae.service;

import com.multi.bungae.domain.BungaeMember;

public interface BungaeMemberService {

    BungaeMember joinBungae();

    BungaeMember createBungaeMember(Long bungae, int user, boolean isOrganizer);
}
