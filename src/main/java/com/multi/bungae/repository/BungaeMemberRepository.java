package com.multi.bungae.repository;

import com.multi.bungae.domain.BungaeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BungaeMemberRepository extends JpaRepository<BungaeMember, Long> {
    
}
