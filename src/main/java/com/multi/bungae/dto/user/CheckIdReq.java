package com.multi.bungae.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ID 중복 체크 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckIdReq {
    private String userId;
}