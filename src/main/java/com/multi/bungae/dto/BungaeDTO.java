package com.multi.bungae.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.multi.bungae.domain.BungaeStatus;
import com.multi.bungae.domain.BungaeType;
import com.multi.bungae.utils.PointSerializer;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
public class BungaeDTO {

    private Long bungaeId;

    private BungaeType bungaeType;
    private String bungaeName;
    private String bungaeDescription;

    @JsonSerialize(using = PointSerializer.class)
    private Point bungaeLocation;
    
    private String bungaeImagePath;
    private int bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private int bungaeMinAge;
    private int bungaeMaxAge;
    private BungaeStatus bungaeStatus;
}
