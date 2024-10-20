package com.smarty.pfeserver.Tools.Configuration;



import com.smarty.pfeserver.Models.Chat.ChatMessage;
import com.smarty.pfeserver.Models.Chat.ChatNotification;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Services.Chat.ChatMessageService;
import com.smarty.pfeserver.Services.Chat.ChatRoomService;
import com.smarty.pfeserver.Services.User.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;
import java.util.Optional;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection "+ event.getMessage());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws InterruptedException, FirebaseMessagingException {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long sender = (Long) headerAccessor.getSessionAttributes().get("sender");
        Long recipient = (Long) headerAccessor.getSessionAttributes().get("recipient");
        if(sender != null) {
            users userssender = userService.findById(sender);
            users usersrecipient = userService.findById(recipient);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContent("User Disconnected : " + userssender);
            chatMessage.setSenderId(userssender.getId());
            chatMessage.setRecipientId(usersrecipient.getId());
            chatMessage.setTimestamp(new Date());
            chatMessage.setRecipientName(usersrecipient.getName());
            ChatMessage saved = chatMessageService.save(chatMessage);

            logger.info("User Disconnected : " + userssender);
            Optional<String> chatId = chatRoomService.getChatId(saved.getSenderId(), saved.getRecipientId(), saved.getContent(),true);
            chatMessage.setChatId(chatId.get());

            messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId().toString(),"/queue/chat",
                    new ChatNotification(
                            saved.getId(),
                            userssender.getProfileimage() != null ? userssender.getProfileimage().getUrl() : "",
                            userssender.getName(),
                            "connection closed",
                            userssender.getId(),
                            saved.getChatId(),
                            saved.getAttachments(),
                            new Date())
            );
        }
    }

}
