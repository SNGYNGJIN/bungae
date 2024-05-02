package com.multi.bungae.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BungaeDTO {

    private int id;

    private int bungaeType;

    // private 장소 bungaeLocation;

    private String imageName;
    private String imagePath;
    private int bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private int bungaeMinAge;
    private int bungaeMaxAge;
    private int bungaeStatus;
}
