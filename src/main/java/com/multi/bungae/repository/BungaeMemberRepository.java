package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeMemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.grammars.hql.HqlParser.SELECT;
import static org.hibernate.sql.ast.Clause.FROM;
import static org.hibernate.sql.ast.Clause.WHERE;

@Repository
public interface BungaeMemberRepository extends JpaRepository<BungaeMember, Long> {

    // 해당 모임에 userId가 존재하는지 여부
    boolean existsByBungaeAndUser(Bungae bungae, UserVO user);

    List<BungaeMember> findByBungae_BungaeId(Long bungaeId);
}
