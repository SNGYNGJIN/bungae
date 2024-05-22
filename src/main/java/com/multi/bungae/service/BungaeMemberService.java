package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;

import java.util.List;

public interface BungaeMemberService {

    BungaeMember joinBungae(Long bungaeId, String userId);

    BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer);

    List<Bungae> findBungaeByUserId(int userId);

    Bungae findBungaeById(int userId);

}
