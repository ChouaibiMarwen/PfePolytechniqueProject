package com.camelsoft.rayaserver.Controller.Chat;


import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.Chat.ChatMessage;
import com.camelsoft.rayaserver.Models.Chat.ChatNotification;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.chat.ChatMessageRequest;
import com.camelsoft.rayaserver.Response.Notification.AdminNotificationResponse;
import com.camelsoft.rayaserver.Services.Chat.ChatMessageService;
import com.camelsoft.rayaserver.Services.Chat.ChatRoomService;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Notification.AdminNotificationServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@CrossOrigin
@RequestMapping(value = "/api/v1/chat")
public class ChatController  extends BaseController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;
    @Autowired private AdminNotificationServices adminNotificationServices;

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @MessageMapping("/chat")
    public ChatMessage processMessage(@Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
       Thread.sleep(1000);
        ChatMessage chatMessage = new ChatMessage();

        if (request.getSenderId() == null) {
            chatMessage.setContent("cant send to same user");
            return chatMessage;
        }
        if ( request.getRecipientId()== null) {
            chatMessage.setContent("cant send to same user");
            return chatMessage;
        }

        Optional<String> chatId = chatRoomService.getChatId(request.getSenderId(), request.getRecipientId(), true);

        if (request.getSenderId() == request.getRecipientId()) {
            chatMessage.setContent("cant send to same user");
            return chatMessage;
        }


       if(chatId.isPresent()){
           Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("sender", request.getSenderId());
           Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("recipient", request.getRecipientId());
           users sender = this.userService.findById(request.getSenderId());
           users reciver = this.userService.findById(request.getRecipientId());
           if(sender==null){
                   chatMessage.setContent("cant send to null user sender");
                   return chatMessage;
               }
         if(reciver==null){
                   chatMessage.setContent("cant send to null user receiver");
                   return chatMessage;
               }
           chatMessage.setChatId(chatId.get());
           chatMessage.setRecipientName(reciver.getName());
           chatMessage.setSenderName(sender.getName());
           chatMessage.setRecipient(reciver);
           chatMessage.setSender(sender);
           chatMessage.setRecipientId(request.getRecipientId());
           chatMessage.setSenderId(request.getSenderId());
           chatMessage.setStatus(MessageStatus.SENDING);
           chatMessage.setTimestamp(new Date());
           chatMessage.setContent(request.getContent());
           chatMessage.setAttachments(chatMessage.getAttachments());
           ChatMessage saved = chatMessageService.save(chatMessage);
           messagingTemplate.convertAndSendToUser(saved.getRecipient().getId().toString(), "/queue/chat",
                   new ChatNotification(
                           saved.getId(),
                           saved.getSender().getProfileimage() != null ? saved.getSender().getProfileimage().getUrl() : "",
                           saved.getSender().getName(),
                           request.getContent(),
                           saved.getSender().getId(),
                           saved.getChatId(),
                           saved.getAttachments(),
                           new Date())
           );
           return chatMessage;
       }else{
           chatMessage.setContent("can't create chat room");
           return chatMessage;
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


    @PostMapping("/add_files")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SUPPLIER')")
    public ResponseEntity<List<File_model>> add_files(@RequestParam(value = "files") List<MultipartFile> files) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        List<File_model> filesw = new ArrayList<>();
        for (MultipartFile file:files) {

            File_model resource_media = filesStorageService.save_file_local(file,  "messages");
            user.getDocuments().add(resource_media);
            filesw.add(resource_media);
            userService.UpdateUser(user);

        }
        return ResponseEntity.ok(filesw);
    }



}
