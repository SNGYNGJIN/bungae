package com.multi.bungae.controller;


import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserReview;
import com.multi.bungae.dto.ReviewDTO;
import com.multi.bungae.dto.ReviewRequest;
import com.multi.bungae.dto.user.forReview;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.ReviewService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private final BungaeMemberRepository bungaeMemberRepo;
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private ReviewService reviewService;

    /*
        번개 모임에 참여했던 사용자 출력하기
     */
    @ResponseBody
    @GetMapping("/{bungaeId}")
    public ResponseEntity<List<forReview>> getBungaeMemberss(@PathVariable Long bungaeId) {
        List<BungaeMember> members = bungaeMemberRepo.findByBungae_BungaeId(bungaeId);
        List<forReview> userNicknames = members.stream()
                .map(member -> new forReview(
                        member.getUser().getUserId(),
                        member.getUser().getNickname(),
                        member.getUser().getProfile().getUserImage()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userNicknames);
    }

    /*
        리뷰 등록하면 해당 리뷰 db에 등록하기
     */
    @ResponseBody
    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveReview(@PathVariable String userId, @RequestBody ReviewDTO review) {
        ReviewDTO savedReview = reviewService.saveReview(userId, review);
        return ResponseEntity.ok(savedReview);
    }

    /*
        내 리뷰만 나열하기
     */
    @ResponseBody
    @GetMapping("/myReview/{userId}")
    public ResponseEntity<List<UserReview>> myReview(@PathVariable String userId) {
        List<UserReview> myReview = reviewService.myReview(userId);
        return ResponseEntity.ok(myReview);
    }

    /*
        사용자가 리뷰를 썼는지 안 썼는지 구별
     */
    @ResponseBody
    @PostMapping("/reviewed")
    public ResponseEntity<Boolean> getReviewed(@RequestBody ReviewRequest reviewRequest) {
        boolean isReviewed = reviewService.hasUserReviewed(reviewRequest);
        return ResponseEntity.ok(isReviewed);
    }

}
