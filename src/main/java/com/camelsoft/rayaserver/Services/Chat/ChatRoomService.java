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

    public Optional<String> getChatId(users sender, users recipient, String lastMessage, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderAndRecipient(sender,recipient)
                .map(ChatRoom::getChatId).or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", sender.getId(), recipient.getId());

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(sender)
                            .recipient(recipient)
                            .lastmessage(lastMessage)
                            .timestmp(new Date())
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(recipient)
                            .recipient(sender)
                            .lastmessage(lastMessage)
                            .timestmp(new Date())
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
