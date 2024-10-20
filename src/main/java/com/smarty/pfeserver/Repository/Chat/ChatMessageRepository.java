package com.smarty.pfeserver.Repository.Chat;




import com.smarty.pfeserver.Enum.Project.Notification.MessageStatus;
import com.smarty.pfeserver.Models.Chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);
    List<ChatMessage> findByChatIdOrderByTimestampAsc(String chatId);
    List<ChatMessage> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    Page<ChatMessage> findByChatIdAndStatusNotOrderByTimestampDesc(Pageable pageable, String chatId, MessageStatus state);
    List<ChatMessage> findByChatIdAndStatusNot(String chatId, MessageStatus state);

}