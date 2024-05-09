package com.camelsoft.rayaserver.Services.Chat;


import com.camelsoft.rayaserver.Models.Chat.ChatRoom;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Chat.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(users senderId, users recipientId, String lastMessage, boolean createIfNotExist) {

         return chatRoomRepository
                .findBySenderAndRecipient(senderId, recipientId)
                .map(ChatRoom::getChatId).or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                     var chatId =
                            String.format("%s_%s", senderId, recipientId);

                     ChatRoom senderRecipient = ChatRoom
                             .builder()
                             .chatId(chatId)
                             .sender(senderId)
                             .recipient(recipientId)
                             .lastmessage(lastMessage)
                             .timestmp(new Date())
                             .build();

                     ChatRoom recipientSender = ChatRoom
                             .builder()
                             .chatId(chatId)
                             .sender(recipientId)
                             .recipient(senderId)
                             .lastmessage(lastMessage)
                             .timestmp(new Date())
                             .build();
                     chatRoomRepository.save(senderRecipient);
                     chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
