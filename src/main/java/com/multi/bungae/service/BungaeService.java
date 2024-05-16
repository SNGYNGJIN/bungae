package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface BungaeService {

    Bungae createBungae(BungaeDTO bungaeDTO, UserVO userVO);

    List<BungaeDTO> bungaeList();

    List<BungaeDTO> findBungaeNearby(Point userLocation, double radius);

    Bungae editBungae(Long bungaeId, BungaeDTO bungaeDTO);

    void cancelBungae(Long bungaeId);

    Bungae cancelBungae2(Long bungaeId);
}
