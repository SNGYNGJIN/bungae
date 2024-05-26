package com.multi.bungae.repository;

import com.multi.bungae.domain.MessageInOffline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageInOfflineRepository extends JpaRepository<MessageInOffline, Long> {
    List<MessageInOffline> findByUserId(String userId);
}