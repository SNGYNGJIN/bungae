package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BungaeRepository extends JpaRepository<Bungae, Long> {
}
