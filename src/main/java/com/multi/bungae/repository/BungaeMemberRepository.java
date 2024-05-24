package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BungaeMemberRepository extends JpaRepository<BungaeMember, Long> {

    Optional<BungaeMember> findByUser(UserVO user);

    @Query("SELECT bm.bungae FROM BungaeMember bm WHERE bm.user.userId = :userId")
    List<Bungae> findBungaeByUserId(@Param("userId") int userId);

    @Query("SELECT bm.bungae FROM BungaeMember bm WHERE bm.user.id = :userId")
    Bungae findBungaeById(@Param("userId") int userId);


    // 해당 모임에 userId가 존재하는지 여부
    boolean existsByUser( UserVO user);


    List<BungaeMember> findByBungae_BungaeId(Long bungaeId);

    boolean existsByBungaeAndUserAndIsOrganizerTrue(Bungae bungae, UserVO user);
    boolean existsByBungaeAndUserAndIsOrganizerFalse(Bungae bungae, UserVO user);
}
