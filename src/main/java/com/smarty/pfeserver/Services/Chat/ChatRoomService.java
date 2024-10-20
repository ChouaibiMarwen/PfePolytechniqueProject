package com.smarty.pfeserver.Services.Chat;


import com.smarty.pfeserver.Models.Chat.ChatRoom;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Chat.ChatRoomRepository;
import com.smarty.pfeserver.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserService userService;

    public Optional<String> getChatId(Long sender, Long recipient, String lastMessage, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(sender,recipient)
                .map(ChatRoom::getChatId).or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    users sendermodel = this.userService.findById(sender);
                    users recivermodel = this.userService.findById(recipient);
                    var chatId =
                            String.format("%s_%s", sender, recipient);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(sendermodel)
                            .recipient(recivermodel)
                            .lastmessage(lastMessage)
                            .timestmp(new Date())
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(recivermodel)
                            .recipient(sendermodel)
                            .lastmessage(lastMessage)
                            .timestmp(new Date())
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
