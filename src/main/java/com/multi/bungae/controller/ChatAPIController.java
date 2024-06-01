package com.multi.bungae.controller;

import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.*;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.SocketStateDTO;
import com.multi.bungae.dto.user.forReview;
import com.multi.bungae.repository.*;
import com.multi.bungae.service.AlarmService;
import com.multi.bungae.service.ChatService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/api")
public class ChatAPIController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService service;
    private final AlarmService alarmService;
    private final UserRepository userRepo;
    private final BungaeRepository bungaeRepo;
    private final BungaeMemberRepository bungaeMemberRepo;
    private final SocketStateRepository socketStateRepo;
    private final MessageInOfflineRepository mioRepo;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);

    /*
        참가했을 경우 기존 채팅방 유저에게 입장 알림
     */
    @GetMapping("/join/{chatRoomId}")
    public ResponseEntity<?> joinChatRoom(@PathVariable Long chatRoomId, @RequestParam String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Bungae bungae = bungaeRepo.findById(chatRoomId).orElseThrow(() -> new UsernameNotFoundException("ChatRoom not found"));

        boolean memberExists = service.checkMemberExists(chatRoomId, userId);

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

    @ResponseBody
    @GetMapping("/chatMember/{bungaeId}")
    public ResponseEntity<List<forReview>> getBungaeMemberss(@PathVariable Long bungaeId) {
        List<BungaeMember> members = bungaeMemberRepo.findByBungae_BungaeId(bungaeId);
        List<forReview> userNicknames = members.stream()
                .map(member -> new forReview(
                        member.getUser().getUserId(),
                        member.getUser().getNickname(),
                        member.getUser().getProfile().getUserImage(),
                        member.isOrganizer()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userNicknames);
    }

    @ResponseBody
    @GetMapping("/findDisconnect/{userId}")
    public ResponseEntity<Boolean> findDisconnect(@PathVariable String userId) {
        UserVO user = userRepo.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by chatAPIController"));

        //Optional<BungaeMember> bungae = bungaeMemberRepo.findByUser_IdAndBungae_BungaeStatusNot(user, );
        Optional<BungaeMember> bungae = bungaeMemberRepo.findByUser_IdAndBungae_BungaeStatusNot(user.getId(), BungaeStatus.ENDED);

        if (bungae.isPresent()) {
            Long bungaeId = bungae.get().getBungae().getBungaeId();
            Optional<SocketState> sock = socketStateRepo.findByChatRoomIdAndUserIdAndState(
                    bungaeId,
                    user.getId(),
                    AbstractEndpoint.Handler.SocketState.valueOf("CLOSED"));

            if (sock.isPresent()) {
                System.out.println("findDisconnect: User is disconnected, returning true .... ");
                return ResponseEntity.ok(true);
            } else {
                System.out.println("findDisconnect: User is connected, returning false .... ");
            }
        } else {
            System.out.println("findDisconnect: BungaeMember not found for user, returning false .... ");
        }

        return ResponseEntity.ok(false);
    }

    @GetMapping("/logoutChat/{userId}")
    public ResponseEntity<List<MessageInOffline>> logoutChat(@PathVariable String userId) {
        List<MessageInOffline> mioList = mioRepo.findByUserId(userId);

        return ResponseEntity.ok(mioList);
    }

    @PostMapping("/checkLogoutMessage/{userId}")
    public ResponseEntity<String> checkLogoutMessage(@PathVariable String userId) {
        List<MessageInOffline> mioList = mioRepo.findByUserId(userId);
        alarmService.readTrue(mioList); // 메소드 호출하여 read를 true로 설정
        return ResponseEntity.ok("Messages marked as read");
    }


}