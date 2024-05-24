package com.multi.bungae.service;

import com.multi.bungae.config.WebSocketChatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);
    //private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 60 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;
    private static final String CONNECTED = "CONNECTED";
    private final static String SSE_NAME = "board";



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

    public void sendAlarm(Long bungaeId, String message, String senderId) {
        Map<String, SseEmitter> userEmitters = emitters.get(bungaeId);
        if (userEmitters != null) {
            userEmitters.forEach((uid, emitter) -> {
                try {
                    List<String> messages = new ArrayList<>();
                    messages.add(message);
                    messages.add(senderId);
                    emitter.send(SseEmitter.event().name("alarm").data(messages));
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
                    emitter.send(SseEmitter.event().name("alarm").data(message));
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
}
