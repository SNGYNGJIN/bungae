package com.multi.bungae.dto;

import groovy.transform.builder.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class MessageInOfflineDTO {
    private String userId;
    private String message;
    private String senderNickname;

}
