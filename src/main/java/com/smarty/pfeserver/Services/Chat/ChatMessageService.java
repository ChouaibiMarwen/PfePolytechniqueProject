package com.smarty.pfeserver.Services.Chat;


import com.smarty.pfeserver.Controller.Chat.ChatController;
import com.smarty.pfeserver.Enum.Project.Notification.MessageStatus;
import com.smarty.pfeserver.Enum.Tools.Action;
import com.smarty.pfeserver.Models.Chat.ChatMessage;
import com.smarty.pfeserver.Models.Notification.Notification;
import com.smarty.pfeserver.Repository.Chat.ChatMessageRepository;
import com.smarty.pfeserver.Response.Chat.ChatMessageResponse;
import com.smarty.pfeserver.Services.Notification.NotificationServices;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import com.smarty.pfeserver.Tools.Exception.ResourceNotFoundException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatMessageService {
    @Autowired private ChatMessageRepository repository;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private NotificationServices _notificationservices;
  @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserService userService;

    private final Log logger = LogFactory.getLog(ChatController.class);

    @Transactional
    public ChatMessage save(ChatMessage chatMessage) throws InterruptedException {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        ChatMessage result = repository.save(chatMessage);

        Notification notificationuser = new Notification(
                this.userService.findById(chatMessage.getSenderId()),
                this.userService.findById(chatMessage.getRecipientId())  ,
                result,
                Action.MESSAGE

        );
        try {
            this._notificationservices.sendnotification(notificationuser,null);
            return chatMessage;
        }  catch (FirebaseMessagingException e) {
            return chatMessage;
        }

    }

    public Long countNewMessages(Long senderId, Long recipientId) {
        return  repository.countBySenderIdAndRecipientIdAndStatus( senderId, recipientId, MessageStatus.RECEIVED);
    }

    public ChatMessageResponse findChatMessages(int page, int size, Long senderId, Long recipientId, Boolean createifnotexist) {
        try {


            var chatId = chatRoomService.getChatId(senderId, recipientId,null, createifnotexist);
            String roomid = "";
            if (chatId.isPresent())
                roomid = chatId.get();
            else
                throw new NotFoundException(String.format("not chat found !!"));
            List<ChatMessage> chat_content = new ArrayList<ChatMessage>();
            Pageable paging = PageRequest.of(page, size);
            Page<ChatMessage> pageTuts = this.repository.findByChatIdAndStatusNotOrderByTimestampDesc(paging, roomid,MessageStatus.REPORTED);
            chat_content = pageTuts.getContent();
            ChatMessageResponse chatresponse = new ChatMessageResponse(
                    chat_content,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            if (pageTuts.getTotalElements() > 0) {
                updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
            }

            return chatresponse;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("chat id not found"));
        }

    }
    public void seenAllMessage(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId,null, false);

        var messages =
                chatId.map(cId -> repository.findByChatIdAndStatusNot(cId,MessageStatus.REPORTED)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

    }

    public ChatMessage findById(Long id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
        List<ChatMessage> result = this.repository.findBySenderIdAndRecipientId(senderId,recipientId);
        List<ChatMessage> resultupdate=new ArrayList<>();
        for (ChatMessage message:result) {
            message.setStatus(status);
            resultupdate.add(message);
        }
        this.repository.saveAll(resultupdate);

    }
}
