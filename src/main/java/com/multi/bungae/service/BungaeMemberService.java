package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;

public interface BungaeMemberService {

    BungaeMember joinBungae();

    BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer);
}
