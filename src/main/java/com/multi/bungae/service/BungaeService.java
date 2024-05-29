package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface BungaeService {


    Bungae getBungaeById(Long bungaeId);

    Bungae createBungae(BungaeDTO bungaeDTO, UserVO userVO);


    List<BungaeDTO> bungaeList();

    List<BungaeDTO> bungaeListOfStartTime();

    List<BungaeDTO> bungaeListOfCreateTime();

    List<BungaeDTO> findBungaeNearby(Point userLocation, double radius);

    Bungae updateBungae(Long bungaeId, BungaeDTO bungaeDTO);

    void cancelBungae(Long bungaeId, UserVO userVO);

    Bungae cancelBungae2(Long bungaeId, UserVO userVO);

    void updateBungaeStatus();

    List<BungaeDTO> search(String keyword);
}
