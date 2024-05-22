package com.multi.bungae.repository;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

/*    List<ChatMessage> findByChatRoomId(Long chatRoomId);
    List<ChatMessage> findByChatRoomId_bungaeIdOrderBySendTimeAsc(Long bungaeId);
    List<ChatMessage> findByChatMessage_bungaeId(Long bungaeId);
    List<ChatMessage> findByChatMessage_ChatRoomId(Long bungaeId);*/

    List<ChatMessage> findByChatRoomId(Long bungaeId);
    boolean existsByChatRoomIdAndSenderAndType(Long chatRoomId, String sender, ChatMessage.MessageType type);

}
