package com.multi.bungae.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private String userId;
    private String reviewerId;
    private Long bungaeId;
}