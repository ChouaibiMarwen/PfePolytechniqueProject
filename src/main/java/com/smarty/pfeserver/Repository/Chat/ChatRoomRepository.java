package com.smarty.pfeserver.Repository.Chat;


import com.smarty.pfeserver.Models.Chat.ChatRoom;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    Optional<ChatRoom> findBySenderAndRecipient(users sender, users recipient);

}
