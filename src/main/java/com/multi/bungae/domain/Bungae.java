package com.multi.bungae.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Bungae {

    @Id
    @Column(name = "bungaeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bungaeId;

    @Column
    private Integer bungaeType;

    // private 장소 bungaeLocation;

    private String bungaeImageName;
    private String bungaeImagePath;
    private Integer bungaeMaxMember;
    private LocalDateTime bungaeCreateTime;
    private LocalDateTime bungaeStartTime;
    private Integer bungaeMinAge;
    private Integer bungaeMaxAge;
    private Integer bungaeStatus;


}
