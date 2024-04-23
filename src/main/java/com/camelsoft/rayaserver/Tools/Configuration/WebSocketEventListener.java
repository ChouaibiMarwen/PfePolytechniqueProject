package com.camelsoft.rayaserver.Tools.Configuration;



import com.camelsoft.rayaserver.Models.Chat.ChatMessage;
import com.camelsoft.rayaserver.Models.Chat.ChatNotification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.Chat.ChatMessageService;
import com.camelsoft.rayaserver.Services.Chat.ChatRoomService;
import com.camelsoft.rayaserver.Services.User.UserService;
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
        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
        String recipient = (String) headerAccessor.getSessionAttributes().get("recipient");
        if(sender != null) {
            users userssender = userService.findByUserName(sender);
            users usersrecipient = userService.findByUserName(recipient);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContent("User Disconnected : " + userssender);
            chatMessage.setSenderId(userssender.getId());
            chatMessage.setRecipientId(usersrecipient.getId());
            chatMessage.setTimestamp(new Date());
            chatMessage.setRecipientName(usersrecipient.getName());
            ChatMessage saved = chatMessageService.save(chatMessage);

            logger.info("User Disconnected : " + userssender);
            Optional<String> chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
            chatMessage.setChatId(chatId.get());

            messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId().toString(),"/queue/chat",
                    new ChatNotification(
                    saved.getId(),
                    saved.getSenderId(),
                    saved.getSenderName()));
        }
    }

}
