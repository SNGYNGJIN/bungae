package com.multi.bungae.repository;

import com.multi.bungae.domain.BlackList;
import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUser_UserId(String userId);
}
