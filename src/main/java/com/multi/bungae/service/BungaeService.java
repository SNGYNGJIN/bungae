package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface BungaeService {

    //    Bungae createBungae(BungaeDTO bungaeDTO, UserVO userVO);
    Bungae createBungae(BungaeDTO bungaeDTO);

    List<Bungae> bungaeList();

    List<Bungae> findBungaeNearby(Point userLocation, double radius);
}
