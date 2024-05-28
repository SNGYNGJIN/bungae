package com.multi.bungae.dto;

import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.BungaeType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class BungaeDTO {

    private Long bungaeId;

    private BungaeType bungaeType;
    private String bungaeName;
    private String bungaeDescription;
    private LocationDTO bungaeLocation;
    private String bungaeImagePath;
    private MultipartFile bungaeImage;
    private int bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private int bungaeMinAge;
    private int bungaeMaxAge;
    private BungaeStatus bungaeStatus;
    private int currentMemberCount;
}