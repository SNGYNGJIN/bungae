package com.multi.bungae.repository;

import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Integer> {
    List<UserReview> findByUser_UserId(String userId);
}
