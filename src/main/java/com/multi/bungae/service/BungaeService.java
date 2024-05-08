package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;

import java.util.List;

public interface BungaeService {

    //    Bungae createBungae(BungaeDTO bungaeDTO, UserVO userVO);
    Bungae createBungae(BungaeDTO bungaeDTO);

    List<Bungae> bungaeList();
}
