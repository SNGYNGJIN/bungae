package com.multi.bungae.repository;

import com.multi.bungae.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
    boolean existsByBlockerId(String blockerId);
    List<BlackList> findByBlockerId(String blockerId);
}
