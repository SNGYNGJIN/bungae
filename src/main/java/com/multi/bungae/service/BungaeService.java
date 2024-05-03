package com.multi.bungae.service;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.dto.BungaeDTO;

public interface BungaeService {

    Bungae createBungae(BungaeDTO bungaeDTO);
}
