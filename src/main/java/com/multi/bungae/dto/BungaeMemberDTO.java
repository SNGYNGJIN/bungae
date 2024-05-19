package com.multi.bungae.dto;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class BungaeMemberDTO {

    private int bungaeMemberId;

    private Long bungaeId;

    private int user;

    private boolean isOrganizer;
}
