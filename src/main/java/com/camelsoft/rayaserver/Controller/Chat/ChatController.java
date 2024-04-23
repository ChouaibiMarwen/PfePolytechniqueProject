package com.camelsoft.rayaserver.Controller.Chat;


import com.camelsoft.rayaserver.Models.Chat.ChatMessage;
import com.camelsoft.rayaserver.Response.Notification.AdminNotificationResponse;
import com.camelsoft.rayaserver.Services.Chat.ChatMessageService;
import com.camelsoft.rayaserver.Services.Chat.ChatRoomService;
import com.camelsoft.rayaserver.Services.Notification.AdminNotificationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;

    @Autowired private AdminNotificationServices adminNotificationServices;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
       Thread.sleep(1000);
        Optional<String> chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
       if(chatId.isPresent()){
           chatMessage.setChatId(chatId.get());
           Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("sender", chatMessage.getSenderId());
           Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("recipient", chatMessage.getRecipientId());
           ChatMessage saved = chatMessageService.save(chatMessage);
           messagingTemplate.convertAndSendToUser(saved.getRecipientId().toString(),"/queue/chat",saved );
       }


    }
    @GetMapping("/messages/{senderId}/{recipientId}/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DRIVER')")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable Long senderId,
            @PathVariable Long recipientId) {

       Long count = chatMessageService.countNewMessages(senderId, recipientId);
        return  new ResponseEntity<>(count,HttpStatus.OK);

    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DRIVER')")
    public ResponseEntity<?> findChatMessages ( @PathVariable Long senderId,
                                                @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
    @PatchMapping("/seenallmessage/{senderId}/{recipientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DRIVER')")
    public ResponseEntity seenallmessage ( @PathVariable Long senderId,
                                                @PathVariable Long recipientId) {
        this.chatMessageService.seenAllMessage(senderId, recipientId);
        return  new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/Getadminnotification/{senderId}/{recipientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminNotificationResponse> Getadminnotification (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @PathVariable Long senderId,
                                                                           @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(adminNotificationServices.get_all_notification_admin(page,size,recipientId,senderId));
    }
    @GetMapping("/GetAlladminnotification/{recipientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminNotificationResponse> GetAlladminnotification (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
                                                                           @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(adminNotificationServices.get_all_all_notification_admin(page,size,recipientId));
    }

    @GetMapping("/messages/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DRIVER')")
    public ResponseEntity<?> findMessage ( @PathVariable Long id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

}
