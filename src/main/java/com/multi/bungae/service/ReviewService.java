package com.multi.bungae.service;

import com.multi.bungae.domain.*;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.ReviewDTO;
import com.multi.bungae.dto.ReviewRequest;
import com.multi.bungae.repository.UserProfileRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.repository.UserReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private UserReviewRepository userReviewRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public ReviewDTO saveReview(String userId, ReviewDTO reviewDto) {

        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .userId(user.getId())
                .reviewerId(reviewDto.getReviewerId())
                .star(reviewDto.getStar())
                .review(reviewDto.getReview())
                .bungaeId(reviewDto.getBungaeId())
                .build();

        UserReview userReview = convertToEntity(reviewDTO);
        userReviewRepo.save(userReview);
        avgRating(userId);

        return reviewDTO;
    }

    private UserReview convertToEntity(ReviewDTO reviewDTO) {

        UserVO user = userRepo.findById(reviewDTO.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserVO reviewer = userRepo.findByUserId(reviewDTO.getReviewerId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProfile userProfile = user.getProfile();

        if (userProfile == null) {
            throw new UsernameNotFoundException("UserProfile not found for user id: " + reviewDTO.getUserId());
        }

        UserReview userReview = new UserReview();
        userReview.setUser(userProfile);
        userReview.setReviewerId(reviewer.getId());
        userReview.setRating(reviewDTO.getStar());
        userReview.setContent(reviewDTO.getReview());
        userReview.setBungaeId(reviewDTO.getBungaeId());

        return userReview;
    }

    public List<UserReview> myReview(String userId){
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userReviewRepo.findByUserId(user.getId());
    }

    public double avgRating(String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<UserReview> reviews = userReviewRepo.findByUserId(user.getId());

        // 평균 평점 계산
        double newRating = reviews.stream()
                .mapToDouble(UserReview::getRating)
                .average()
                .orElse(0.0);  // 리스트가 비어있거나 null일 때?

        DecimalFormat df = new DecimalFormat("#.0"); // 소수점 첫째짜리까지
        newRating = Double.parseDouble(df.format(newRating)); // 다시 double 형으로

        updateUserRatingInProfile(user, newRating);

        return newRating;
    }

    private void updateUserRatingInProfile(UserVO user, double rating) {
        user.getProfile().setUserRating(rating);  // 사용자 프로필의 평점 설정
        userRepo.save(user);  // 변경된 사용자 정보 저장
    }

    // 리뷰 작성 여부 확인 메소드
    public boolean hasUserReviewed(ReviewRequest reviewRequest) throws UsernameNotFoundException {
        UserVO user = userRepo.findByUserId(reviewRequest.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + reviewRequest.getUserId()));
        UserVO reviewer = userRepo.findByUserId(reviewRequest.getReviewerId())
                .orElseThrow(() -> new UsernameNotFoundException("Reviewer not found with ID: " + reviewRequest.getReviewerId()));

        return userReviewRepo.existsByBungaeIdAndReviewerIdAndUserId(reviewRequest.getBungaeId(), reviewer.getId(), user.getId());
    }


}
