package com.multi.bungae.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bungae {

    @Id
    @Column(name = "bungaeId")
    private Long bungaeId;

    @Column
    private Integer bungaeType;

    // private 장소 bungaeLocation;

    private String bungaeImageName;
    private String bungaeImagePath;
    private Integer bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private Byte bungaeMinAge;
    private Byte bungaeMaxAge;
    private Byte bungaeStatus;


}
