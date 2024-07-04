package com.camelsoft.rayaserver.Services.Notification;

import com.google.firebase.messaging.FirebaseMessaging;

import com.camelsoft.rayaserver.Request.Tools.Note;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;


@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    private FirebaseMessaging firebaseMessaging;

    public FCMService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotification(Note note, String token) throws FirebaseMessagingException {
        // Ensure note and token are not null
        if ( token == null) {
            throw new IllegalArgumentException(" token must not be null");
        }
         if (note == null || note.getData().isEmpty() ) {
            throw new IllegalArgumentException("Note   must not be null");
        }

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        // Create a new map and filter out null values
        Map<String, String> data = note.getData().entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(data)
                .build();

        return firebaseMessaging.send(message);
    }

    public Boolean isValidFCMToken(String fcmToken) {
        Message message = Message.builder().setToken(fcmToken).build();
        try {
            firebaseMessaging.send(message,true);
            return true;
        } catch (FirebaseMessagingException fme) {
            return false;
        }
    }
}
