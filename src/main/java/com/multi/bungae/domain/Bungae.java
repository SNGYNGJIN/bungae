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
    @Column(name = "bungae_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bungaeId;

    @Column(name = "bungae_type", nullable = false)
    private Integer bungaeType;

    @Column(name = "bungae_name", nullable = false)
    private String bungaeName;

//    @Column(name = "bungae_id", nullable = false)
//    private 장소 bungaeLocation;

    @Column(name = "bungae_image_name")
    private String bungaeImageName;

    @Column(name = "bungae_image_path")
    private String bungaeImagePath;

    @Column(name = "bungae_max_member", nullable = false)
    private Integer bungaeMaxMember;

    @Column(name = "bungae_create_time", nullable = false)
    private LocalDateTime bungaeCreateTime;

    @Column(name = "bungae_start_time", nullable = false)
    private LocalDateTime bungaeStartTime;

    @Column(name = "bungae_min_age")
    private Integer bungaeMinAge;

    @Column(name = "bungae_max_age")
    private Integer bungaeMaxAge;

    @Column(name = "bungae_status")
    private Integer bungaeStatus;

}
