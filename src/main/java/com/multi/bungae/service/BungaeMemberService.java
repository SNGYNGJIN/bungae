package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.UserVO;
import org.apache.catalina.User;

import java.util.List;
import java.util.Optional;

public interface BungaeMemberService {

    BungaeMember joinBungae(Long bungaeId, String userId);

    BungaeMember createBungaeMember(Bungae bungae, UserVO user, boolean isOrganizer);

    Bungae findBungaeById(int userId);

    List<Bungae> findBungaeByUserId(int userId);

    List<BungaeMember> findByUserId(int userId);

    boolean canJoinOrHostBungae(UserVO user);

    Bungae findActiveBungaeByUserId(int userId);

    int countByBungae_BungaeId(Long bungaeId);

    Optional<BungaeMember> getOrganizerByBungaeId(Long bungaeId);


    boolean isOrganizerTrue(Long bungaeId, String userId);

    boolean isOrganizerFalse(Long bungaeId, String userId);
}
