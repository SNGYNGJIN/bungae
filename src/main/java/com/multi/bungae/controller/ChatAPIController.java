package com.multi.bungae.controller;

import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.ChatRoomDTO;
import com.multi.bungae.service.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatAPIController {

    private final ChatService service;

    @PostMapping
    public ChatRoomDTO createRoom(@RequestParam String name){
        return service.createRoom(name);
    }

    @GetMapping
    public List<ChatRoomDTO> findAllRooms(){
        return service.findAllRoom();
    }
}
