package com.multi.bungae.controller;

import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.SocketState;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.AlarmService;
import com.multi.bungae.service.SocketStateService;
import com.multi.bungae.service.UserService;
import groovy.util.logging.Slf4j;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class SSEController {
    @Autowired
    private UserService userService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BungaeMemberRepository bungaeMemberRepo;
    @Autowired
    private SocketStateService socketService;

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);

    @GetMapping(path = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String userId){
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<BungaeMember> bungae = bungaeMemberRepo.findByUser(user);
        if (bungae.isEmpty()) {
            throw new IllegalStateException("Bungae not found for user");
        }

        // SseEmitter는 서버에서 클라이언트로 이벤트를 전달
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        alarmService.addEmitter(bungae.get().getBungae().getBungaeId(), userId, emitter);

        // 즉시 알림 전송
        // alarmService.sendAlarm("연결됨 .....");

        return emitter;
    }

    @DeleteMapping("/unsubscribe/{userId}")
    public void unsubscribe(@PathVariable String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<BungaeMember> bungae = bungaeMemberRepo.findByUser(user);
        if (bungae.isEmpty()) {
            throw new IllegalStateException("Bungae not found for user");
        }

        alarmService.disconnectUser(bungae.get().getBungae().getBungaeId(), userId);
    }

}
