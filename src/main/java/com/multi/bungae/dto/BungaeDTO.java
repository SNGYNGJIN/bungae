package com.multi.bungae.dto;

import com.multi.bungae.domain.BungaeType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BungaeDTO {

    private int id;

    private BungaeType bungaeType;
    private String bungaeName;
    // private 장소 bungaeLocation;
    private String bungaeImageName;
    private String bungaeImagePath;
    private int bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private int bungaeMinAge;
    private int bungaeMaxAge;
    private int bungaeStatus;
}
