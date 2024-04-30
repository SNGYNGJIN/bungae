package com.multi.bungae.service;

import com.multi.bungae.repository.BungaeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BungaeServiceImpl implements BungaeService {

    @Autowired
    private BungaeRepository bungaeRepository;
}
