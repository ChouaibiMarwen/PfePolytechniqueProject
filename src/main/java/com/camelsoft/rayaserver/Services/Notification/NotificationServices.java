package com.camelsoft.rayaserver.Services.Notification;



import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Enum.Tools.Action;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.DTO.NotificationDto;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Tools.NotificationRepository;
import com.camelsoft.rayaserver.Request.Tools.Note;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationServices {
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private FCMService pushNotificationService;
    public Notification save(Notification model){
        try {
            return this.repository.save(model);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException("not found data");
        }
    }
    public boolean existbyid(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("not found data"));
        }
    }
     public void deletebyid(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("not found data"));
        }
    }
    public Notification update(Notification model){
        try {
            return this.repository.save(model);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException("not found data");
        }
    }
    public Notification findbyid(Long id){
        try {
            return this.repository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw  new NotFoundException("not found data");
        }
    }
    /*public PaginationResponse allnotificationbyuser(int page, int size, users user) {*/
    public DynamicResponse allnotificationbyuser(int page, int size, users user) {
        try {
            List<Notification> resultlist = this.repository.findAllByReciver(user);
            List<NotificationDto> resultdto = resultlist.stream()
                    .map(NotificationDto::NotificationToDto).collect(Collectors.toList());

            Pageable pageable = PageRequest.of(page, size);
            int start = Math.min((int) pageable.getOffset(), resultdto.size());
            int end = Math.min((start + pageable.getPageSize()), resultdto.size());
            Page<NotificationDto> usersPage = new PageImpl<>(resultdto.subList(start, end), pageable, resultdto.size());
            DynamicResponse dynamicresponse = new DynamicResponse(usersPage.getContent(), usersPage.getNumber(), usersPage.getTotalElements(), usersPage.getTotalPages());
            return dynamicresponse;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public DynamicResponse waitinGnOTIFICATIONSuSER(int page, int size, users user) {
        try {
            List<Notification> resultlist = this.repository.findAllByReciverAndStatusNot(user, MessageStatus.READ);
            List<NotificationDto> resultdto = resultlist.stream()
                    .map(NotificationDto::NotificationToDto).collect(Collectors.toList());

            Pageable pageable = PageRequest.of(page, size);
            int start = Math.min((int) pageable.getOffset(), resultdto.size());
            int end = Math.min((start + pageable.getPageSize()), resultdto.size());
            Page<NotificationDto> usersPage = new PageImpl<>(resultdto.subList(start, end), pageable, resultdto.size());
            DynamicResponse dynamicresponse = new DynamicResponse(usersPage.getContent(), usersPage.getNumber(), usersPage.getTotalElements(), usersPage.getTotalPages());
            return dynamicresponse;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    private void changenotificationstate( List<Notification> notification){
        for (Notification nf:notification) {
            nf.setStatus(MessageStatus.DELIVERED);
            this.update(nf);

        }

    }
    public PaginationResponse allnotificationbyuserbystate(int page, int size, users user, MessageStatus status) {
        try {
            List<Notification> resultlist = new ArrayList<Notification>();
            Pageable paging = PageRequest.of(page, size);
            Page<Notification> pageTuts = this.repository.findAllByReciverAndStatus(paging,user,status);
            resultlist = pageTuts.getContent();
            PaginationResponse response = new PaginationResponse(
                    resultlist,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            this.changenotificationstate(resultlist);
            return response;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }
    private String converMessageenum(Action action) {
        switch (action) {
            case MESSAGE:
                return "You have new message";
            case LOAN:
                return "You have new loan";
            case INVOICE:
                return "You have new invoice";
            case PURCHASE:
                return "You have new purchase";
            case SUPPLIER:
                return "You have new supplier";
            case CUSTOMER:
                return "You have new customer";
            default:
                return "You have new notification";
        }
    }
 /*   public void sendnotification(Notification notificationuser,Notification notificationadmin) throws InterruptedException, FirebaseMessagingException {
        Action action=Action.IDLE;
        if(notificationuser!=null){
            Notification resultadminnotificationuser =  this.save(notificationuser);
            action=resultadminnotificationuser.getAction();
            messagingTemplate.convertAndSendToUser(resultadminnotificationuser.getReciver().getId().toString(),"/queue/user", resultadminnotificationuser);
            Thread.sleep(1000);
            Note note = new Note();
            note.setSubject(action.name());
            note.setContent(notificationuser.getContent());
            note.setData(notificationuser.toMap());
            if(!this.userDeviceService.findbyuserdevice(notificationuser.getReciver()).isEmpty()){
                List<UserDevice> devices = this.userDeviceService.findbyuserdevice(notificationuser.getReciver());
                for (UserDevice device: devices) {
                    if(device!=null && device.getTokendevice()!=null){
                        if(note!=null && this.pushNotificationService.isValidFCMToken(device.getTokendevice())){

                            this.pushNotificationService.sendNotification(note,device.getTokendevice());
                            Thread.sleep(1000);
                        }else{
                            this.userDeviceService.deletebyid(device.getId());

                        }

                    }
                }


            }

        }
        if(notificationadmin!=null){
            List<users> admins = this.userService.findAllAdmin();
            for (users admin:admins) {
                notificationadmin.setReciver(admin);
                Notification resultadminnotification =  this.save(notificationadmin);
                messagingTemplate.convertAndSendToUser(resultadminnotification.getReciver().getId().toString(),"/queue/admin", resultadminnotification);
                Thread.sleep(1000);

            }
        }

    }*/

    public void sendnotification(Notification notificationuser, Notification notificationadmin) throws InterruptedException, FirebaseMessagingException {
        Action action = Action.IDLE;

        if (notificationuser != null) {
            Notification resultadminnotificationuser = this.save(notificationuser);
            action = resultadminnotificationuser.getAction();
            messagingTemplate.convertAndSendToUser(resultadminnotificationuser.getReciver().getId().toString(), "/queue/user", resultadminnotificationuser);
            Thread.sleep(1000);

            Note note = new Note();
            note.setSubject(action.name());
            note.setContent(notificationuser.getContent());
            note.setData(notificationuser.toMap());

            // Track sent devices to avoid duplicates
            Set<String> sentDevices = new HashSet<>();

            List<UserDevice> devices = this.userDeviceService.findbyuserdevice(notificationuser.getReciver());

            for (UserDevice device : devices) {
                if (device != null && device.getTokendevice() != null && this.pushNotificationService.isValidFCMToken(device.getTokendevice())) {
                    // Check if device has already received a notification
                    if (!sentDevices.contains(device.getTokendevice())) {
                        this.pushNotificationService.sendNotification(note, device.getTokendevice());
                        Thread.sleep(1000);
                        sentDevices.add(device.getTokendevice()); // Track sent device
                    }
                } /*else {
                    // Handle invalid tokens or remove invalid devices from database
                   // this.userDeviceService.deletebyid(device.getId());
                    System.out.println("Invalid FCM token");
                }*/
            }
        }

        if (notificationadmin != null) {
            List<users> admins = this.userService.findAllAdmin();
            for (users admin : admins) {
                notificationadmin.setReciver(admin);
                Notification resultadminnotification = this.save(notificationadmin);
                messagingTemplate.convertAndSendToUser(resultadminnotification.getReciver().getId().toString(), "/queue/admin", resultadminnotification);
                Thread.sleep(1000);
            }
        }
    }

    private String generateUniqueDeviceIdentifier(UserDevice device) {
        if (device == null) {
            return null;
        }
        // Example: Combine relevant attributes to create a unique identifier for the physical device
        return device.getDeviceType() + "_" + device.getDeviceId();
    }

    public void sendnotifications(Notification notificationuser, List<users> usersList) throws InterruptedException, FirebaseMessagingException {
        Action action = notificationuser.getAction();

        Set<String> processedTokens = new HashSet<>();
        for (users user : usersList) {
            notificationuser.setReciver(user);
            Note note = new Note();
            note.setSubject(action == null ? notificationuser.getSubject() : action.name());
            action = notificationuser.getAction() == null ? notificationuser.getAction() : action;
            notificationuser.setAction(action);
            Thread.sleep(1000);
            note.setContent(notificationuser.getContent());
            if (!this.userDeviceService.findbyuserdevice(notificationuser.getReciver()).isEmpty() && !notificationuser.getSender().getId().equals(user.getId())) {
                List<UserDevice> devices = this.userDeviceService.findbyuserdevice(notificationuser.getReciver());
                for (UserDevice device : devices) {
                    if (device != null && device.getTokendevice() != null) {
                        if (this.pushNotificationService.isValidFCMToken(device.getTokendevice())) {
                            if (!processedTokens.contains(device.getTokendevice())) {
                                notificationuser = this.save(notificationuser);
                                note.setData(notificationuser.toMap());
                                this.pushNotificationService.sendNotification(note, device.getTokendevice());
                                processedTokens.add(device.getTokendevice());
                            }
                            Thread.sleep(1000);
                        } else {
                            this.userDeviceService.deletebyid(device.getId());
                        }
                    }
                }
            }
            processedTokens.clear();
        }

    }
}
