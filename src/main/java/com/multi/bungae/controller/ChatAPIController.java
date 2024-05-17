package com.multi.bungae.controller;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseResponse;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.user.UserProfileDTO;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.ChatService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/api")
public class ChatAPIController {

    private final ChatService service;
    private final UserRepository userRepo;

    @PostMapping("/join")
    public ChatDTO joinChat(@RequestParam Long chatRoomId, HttpSession session){
        Integer id = (Integer) session.getAttribute("loggedInId");
        UserVO user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return service.joinChat(chatRoomId, user.getUserId());
    }

    @GetMapping
    public List<ChatDTO> findAllRooms(){

        return service.findAllRoom();
    }

/*    @PostMapping("/{chatRoomId}")
    public chatDTO accessRoom(@RequestParam Long chatRoomId) {
        return service.accessRoom(chatRoomId);
    }*/



}
