package com.camelsoft.rayaserver.Services.Notification;

import com.google.firebase.messaging.FirebaseMessaging;

import com.camelsoft.rayaserver.Request.Tools.Note;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    private FirebaseMessaging firebaseMessaging;

    public FCMService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotification(Note note, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData())
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
