package com.multi.bungae.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private int userId;
    private String reviewerId;
    private Long bungaeId;
    private double star;
    private String review;
}
