package com.multi.bungae.service;

import com.multi.bungae.config.WebSocketChatHandler;
import com.multi.bungae.controller.SSEController;
import com.multi.bungae.domain.*;
import com.multi.bungae.dto.ChatDTO;
import com.multi.bungae.dto.SocketStateDTO;
import com.multi.bungae.repository.*;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SocketStateService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketChatHandler.class);

    @Autowired
    private SocketStateRepository socketStateRepo;
    private final Map<String, Set<WebSocketSession>> chatSessions = new ConcurrentHashMap<>();
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private UserService userService;

    /*
        처음 입장했을 때 createStateOpen을 호출하기 !
     */
    public SocketStateDTO createStateOpen(Long chatRoomId, String userId) {
        UserVO user = userRepo.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SocketStateDTO state = SocketStateDTO.builder()
                .chatRoomId(chatRoomId)
                .userId(user.getId())
                .state(AbstractEndpoint.Handler.SocketState.valueOf("OPEN"))
                .closedTime(null)
                .build();

        SocketState socketState = convertToEntity(state);
        socketStateRepo.save(socketState);
        log.debug("Converted SocketState: {}", socketState);

        return state;
    }

    private SocketState convertToEntity(SocketStateDTO socketDTO) {
        SocketState socket = new SocketState();

        socket.setChatRoomId(socketDTO.getChatRoomId());
        socket.setUserId(socketDTO.getUserId());
        socket.setState(socketDTO.getState());
        socket.setClosedTime(socketDTO.getClosedTime());

        return socket;
    }

    /*
        유저가 채팅방에 재입장 했을 때, State를 Open으로 바꿈
     */
    public SocketStateDTO updateStateOpen(SocketState socketState) {
        socketState.setState(AbstractEndpoint.Handler.SocketState.valueOf("OPEN"));
        socketState.setClosedTime(null);

        SocketState updatedState = socketStateRepo.save(socketState);

        SocketStateDTO dto = new SocketStateDTO();
        dto.setState(updatedState.getState());
        dto.setClosedTime(null);

        return dto;
    }


    /*
        유저가 채팅방을 나갔을 때, State를 CLOSED로 바꾸고 나간 시간 저장
     */
    public SocketStateDTO updateStateClosed(SocketState socketState) {
        LocalDateTime closedTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        socketState.setState(AbstractEndpoint.Handler.SocketState.valueOf("CLOSED"));
        socketState.setClosedTime(closedTime);

        SocketState updatedState = socketStateRepo.save(socketState);

        SocketStateDTO dto = new SocketStateDTO();
        dto.setState(updatedState.getState());
        dto.setClosedTime(updatedState.getClosedTime());

        return dto;
    }

    /*
        해당 방 안에서 웹소켓 연결 상태가 CLOSED인 멤버 반환
     */
    public List<SocketState> findClosedUser(Long bungaeId){
        return socketStateRepo.findByChatRoomIdAndState(bungaeId, AbstractEndpoint.Handler.SocketState.valueOf("CLOSED"));
    }

    /*
        CLOSED 상태인 유저들에게 알람 보내기
     */
    public void chatAlarm(Long bungaeId, String input, String senderId) {
        UserVO user = userRepo.findByUserId(senderId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        alarmService.sendAlarm(bungaeId, input, user.getNickname());
    }

}
