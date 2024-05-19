package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.BungaeRepository;
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
    private final BungaeRepository bungaeRepo;

    @GetMapping("/join/{chatRoomId}")
    public ResponseEntity<Boolean> joinChatRoom(@PathVariable Long chatRoomId, @RequestParam String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));
        boolean memberExists = service.checkMemberExists(chatRoomId, user.getId());

        if (!memberExists) {
            service.joinChat(chatRoomId, userId);
        }

        return ResponseEntity.ok(memberExists);
    }





    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessage> messages = service.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(messages);
    }
}