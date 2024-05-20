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

    /*
        참가했을 경우 기존 채팅방 유저에게 입장 알림
     */
    @GetMapping("/join/{chatRoomId}")
    public ResponseEntity<?> joinChatRoom(@PathVariable Long chatRoomId, @RequestParam String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));

        boolean memberExists = service.checkMemberExists(bungae, user);

        if (!memberExists) {
            ChatDTO chat = service.joinChat(chatRoomId, userId);
            messagingTemplate.convertAndSend("/room/" + chatRoomId, chat);

            return ResponseEntity.ok().body(chat);
        }

        return ResponseEntity.ok().body("방 참가 여부 : " + memberExists);
    }

    /*
        chatRoomId에 해당하는 채팅메세지 불러오기 기능 (늦게 들어온 사람도 볼 수 있게)
     */
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessage> messages = service.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(messages);
    }

}