package com.multi.bungae.controller;

import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.domain.SocketState;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.repository.SocketStateRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.AlarmService;
import com.multi.bungae.service.ChatService;
import com.multi.bungae.service.SocketStateService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final UserRepository userRepo;
    private final SocketStateRepository socketStateRepo;
    private final ChatService chatService;
    private final SocketStateService socketService;
    private final AlarmService alarmService;

    @GetMapping("/{chatRoomId}")
    public String getChatRoom(@PathVariable Long chatRoomId, Model model) {
        return "chatting"; // chatting.html 뷰 반환
    }

    // WebSocket 핸들러는 메시지 브로드캐스트만 담당
    @MessageMapping("/{roomId}") // 여기(send/{roomId})로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   // 구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatDTO chat(@DestinationVariable Long roomId, ChatDTO input) {
        //alarmService.viewAlarm(roomId, input.getMessage(), input.getSender());
        socketService.chatAlarm(roomId, input.getMessage(), input.getSender());

        return chatService.ChatMessage(roomId, input.getSender(), input.getMessage(), input.getType());
    }

    @MessageMapping("/enter/{roomId}/{userId}")
    @SendTo("/room/{roomId}")
    public String enter(@DestinationVariable Long roomId, @DestinationVariable String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SocketState state = socketStateRepo.findByChatRoomIdAndUserId(roomId, user.getId());

        socketService.updateStateOpen(state);
        //alarmService.removeEmitterForSession(userId);
        return "{\"type\":\"enter\", \"message\":\"" + userId + "님이 입장했어요\"}";
    }

    @MessageMapping("/leave/{roomId}/{userId}")
    @SendTo("/room/{roomId}")
    public String leave(@DestinationVariable Long roomId, @DestinationVariable String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SocketState state = socketStateRepo.findByChatRoomIdAndUserId(roomId, user.getId());

        socketService.updateStateClosed(state);
        //SseEmitter emitter = new SseEmitter();
        //alarmService.addEmitter(userId, emitter);
        return "{\"type\":\"leave\", \"message\":\"" + userId + "님이 떠났어요\"}";
    }


    private String filterMessage(String message) {
        return message.replaceAll("금지된 단어", "***");
    }
}