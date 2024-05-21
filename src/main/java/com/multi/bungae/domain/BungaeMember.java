package com.multi.bungae.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bungaeMemberId")
public class BungaeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bungaeMemberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bungae_id")
    @JsonBackReference
    private Bungae bungae;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserVO user;

    private boolean isOrganizer;

}
