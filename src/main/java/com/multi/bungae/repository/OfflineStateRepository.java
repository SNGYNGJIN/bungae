package com.multi.bungae.repository;

import com.multi.bungae.domain.OfflineState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfflineStateRepository extends JpaRepository<OfflineState, Integer> {

    Optional<OfflineState> findById(int userId);

    List<OfflineState> findByState(OfflineState.State state);

    OfflineState findByUserId(int id);
}