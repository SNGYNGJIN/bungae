package com.multi.bungae.controller;

import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.ChatMessage;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.repository.ChatMessageRepository;
import com.multi.bungae.service.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatMessageRepository chatMessageRepo;
    private final ChatService chatService;
    private static final Logger log = LoggerFactory.getLogger(WebSocketChatHandler.class);

    @GetMapping("/{chatRoomId}")
    public String getChatRoom(@PathVariable Long chatRoomId, Model model) {
        return "chatting"; // chatting.html 뷰 반환
    }

    @GetMapping("/example")
    public String getChatRoom() {
        return "example"; // chatting.html 뷰 반환
    }

    @MessageMapping("/{roomId}") //여기(send/{roomId})로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatDTO chat(@DestinationVariable Long roomId, ChatDTO input) {
        // ChatMessageInput은 클라이언트로부터 받은 메시지 데이터를 담는 DTO
        return chatService.ChatMessage(roomId, input.getSender(), input.getMessage());
    }

    private String filterMessage(String message) {
        return message.replaceAll("금지된 단어", "***");
    }
}

