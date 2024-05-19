package com.multi.bungae.controller;

import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.ChatService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/api")
public class ChatAPIController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService service;
    private final UserRepository userRepo;
    private final BungaeMemberRepository bungaeMemberRepo;

    @GetMapping("/join/{chatRoomId}")
    public ResponseEntity<Boolean> joinChatRoom(@PathVariable Long chatRoomId, @RequestParam String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean memberExists = service.checkMemberExists(chatRoomId, user.getId());
        if (!memberExists) {
            ChatDTO chat = service.joinChat(chatRoomId, userId);
            messagingTemplate.convertAndSend("/room/broadcast/" + chatRoomId, chat);
        }
        return ResponseEntity.ok(memberExists);
    }


    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessage> messages = service.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(messages);
    }


/*    @PostMapping("/{chatRoomId}")
    public chatDTO accessRoom(@RequestParam Long chatRoomId) {
        return service.accessRoom(chatRoomId);
    }*/
}