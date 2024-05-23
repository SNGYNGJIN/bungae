package com.multi.bungae.repository;

import com.multi.bungae.domain.SocketState;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocketStateRepository extends JpaRepository<SocketState, Long> {

    SocketState findByChatRoomIdAndUserId(Long chatRoomId, int userId);

    List<SocketState> findByChatRoomIdAndState(Long chatRoomId, AbstractEndpoint.Handler.SocketState state);

}