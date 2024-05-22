package com.multi.bungae.dto.user;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class forReview {
    private String userId;
    private String nickname;
    private String userImage;
    private boolean organizer;

}
