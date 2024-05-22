package com.multi.bungae.repository;

import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import com.multi.bungae.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Integer> {
    List<UserReview> findByUserId(int userId);
    boolean existsByBungaeIdAndReviewerIdAndUserId(Long bungaeId, int reviewerId, int userId);


}
