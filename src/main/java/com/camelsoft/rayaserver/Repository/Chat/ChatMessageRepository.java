package com.camelsoft.rayaserver.Repository.Chat;




import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.Chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);
    List<ChatMessage> findByChatIdOrderByTimestampAsc(String chatId);
    List<ChatMessage> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}