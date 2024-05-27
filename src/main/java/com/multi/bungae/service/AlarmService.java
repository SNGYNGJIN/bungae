package com.multi.bungae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.domain.*;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.MessageInOfflineDTO;
import com.multi.bungae.dto.user.forReview;
import com.multi.bungae.repository.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import javax.management.Notification;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Service
public class AlarmService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageInOfflineRepository messageRepo;
    @Autowired
    private BungaeMemberRepository bungaeMemberRepo;
    @Autowired
    private OfflineStateRepository offRepo;

    //private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long bungaeId, String userId, SseEmitter emitter) {
        emitters.computeIfAbsent(bungaeId, k -> new ConcurrentHashMap<>()).put(userId, emitter);
        emitters.forEach((key, value) -> logger.info("BungaeID: {}, UserID-Emitter Map: {}", key, value));

        emitter.onCompletion(() -> removeEmitter(bungaeId, userId));
        emitter.onTimeout(() -> removeEmitter(bungaeId, userId));
        emitter.onError(e -> removeEmitter(bungaeId, userId));
    }

    public void removeEmitter(Long bungaeId, String userId) {
        Map<String, SseEmitter> userEmitters = emitters.get(bungaeId);
        if (userEmitters != null) {
            userEmitters.remove(userId);
            if (userEmitters.isEmpty()) {
                emitters.remove(bungaeId);
            }
        }
    }

    public void sendAlarm(Long bungaeId, String message, String senderNickname) {
        Map<String, SseEmitter> userEmitters = emitters.get(bungaeId);
        viewAlarm(bungaeId, message, senderNickname);
        if (userEmitters != null) {
            userEmitters.forEach((uid, emitter) -> {
                try {

                    Map<String, String> messages = new HashMap<>();
                    messages.put("message", message);
                    messages.put("senderId", senderNickname);
                    messages.put("url", "/chat/"+bungaeId);

                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonMessages = objectMapper.writeValueAsString(messages);

                    emitter.send(SseEmitter.event().name("alarm").data(jsonMessages));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        }
    }

    public void sendAlarm(String message) {
        emitters.forEach((bungaeId, userEmitters) -> {
            userEmitters.forEach((userId, emitter) -> {
                try {
                    Map<String, String> messages = new HashMap<>();
                    messages.put("message", message);
                    messages.put("senderId", null);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonMessages = objectMapper.writeValueAsString(messages);
                    emitter.send(SseEmitter.event().name("alarm").data(jsonMessages));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        });
    }


    public void disconnectUser(Long bungaeId, String userId) {
        removeEmitter(bungaeId, userId);
        System.out.println("User " + userId + " disconnected from Bungae " + bungaeId);
    }

    /*
        사용자가 로그아웃 상태일 동안 도착한 메세지 저장
     */
    @Transactional
    public void viewAlarm(Long bungaeId, String message, String senderNickname) {
        System.out.println("viewAlarm까지 도착하긴 함..");

        List<BungaeMember> bungaeMembers = bungaeMemberRepo.findByBungae_BungaeId(bungaeId);
        Set<String> offlineUserIds = new HashSet<>();

        for (BungaeMember bungaeMember : bungaeMembers) {
            UserVO user = bungaeMember.getUser();
            OfflineState offlineState = offRepo.findByUserId(user.getId());
            if (offlineState != null && offlineState.getState() == OfflineState.State.OFFLINE) {
                offlineUserIds.add(user.getUserId());
            }
        }

        for (String userId : offlineUserIds) {
            MessageInOffline mio = new MessageInOffline();
            mio.setUserId(userId);
            mio.setMessage(message);
            mio.setSenderNickname(senderNickname);
            mio.setSendedTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            mio.setRead(false);
            messageRepo.save(mio);
        }
    }

    public void readTrue(List<MessageInOffline> mioList) {
        for (MessageInOffline mio : mioList) {
            mio.setRead(true);
        }
        messageRepo.saveAll(mioList);
    }

}
