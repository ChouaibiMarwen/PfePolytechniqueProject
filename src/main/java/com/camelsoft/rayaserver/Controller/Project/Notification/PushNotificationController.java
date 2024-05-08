package com.camelsoft.rayaserver.Controller.Project.Notification;

import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.Notification.FCMService;
import com.camelsoft.rayaserver.Services.Notification.NotificationServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/notifications")
public class PushNotificationController {

    @Autowired
    private NotificationServices _notificationservices;
    @Autowired
    private FCMService pushNotificationService;
    @Autowired
    private UserService userService;


   /* @PostMapping("/send-notification")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SUPPLIER')")
    public ResponseEntity sendNotification(@RequestBody N request) throws FirebaseMessagingException {
        if (!this.pushNotificationService.isValidFCMToken(request.getToken()))
            return new ResponseEntity("Invalid FCM Token", HttpStatus.BAD_REQUEST);
        try {
            pushNotificationService.sendNotification(request.getNote(), request.getToken());
            return new ResponseEntity("200 Ok sending notification", HttpStatus.OK);
        } catch (FirebaseMessagingException e) {
            return new ResponseEntity("Error sending notification", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send-notification-test")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SUPPLIER')")
    public ResponseEntity sendnotificationtest(@RequestBody notificationrequest request) throws FirebaseMessagingException {


        users user = this.userService.findByUserName(getCurrentUser().getUsername());

        notificationmodel notificationuser = new notificationmodel(user, user, "Action.IDLE","");
        try {
            List<users> usersList = new ArrayList<>();
            usersList.add(user);
            this._notificationservices.sendnotifications(notificationuser,usersList);
        } catch (FirebaseMessagingException | InterruptedException ex ) {
            return new ResponseEntity("Error sending notification", HttpStatus.EXPECTATION_FAILED);

        }
        return new ResponseEntity("Notification Sent", HttpStatus.OK);

    }*/
}
