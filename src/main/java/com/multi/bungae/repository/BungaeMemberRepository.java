package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BungaeMemberRepository extends JpaRepository<BungaeMember, Long> {

    // 해당 모임에 userId가 존재하는지 여부
    boolean existsByBungaeAndUser(Bungae bungae, UserVO user);
}
