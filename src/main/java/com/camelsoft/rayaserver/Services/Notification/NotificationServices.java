package com.camelsoft.rayaserver.Services.Notification;



import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Enum.Tools.Action;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Tools.NotificationRepository;
import com.camelsoft.rayaserver.Request.Tools.Note;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public PaginationResponse allnotificationbyuser(int page, int size, users user) {
        try {
            List<Notification> resultlist = new ArrayList<Notification>();
            Pageable paging = PageRequest.of(page, size);
            Page<Notification> pageTuts = this.repository.findAllByReciver(paging,user);
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
    public void sendnotification(Notification notificationuser,Notification notificationadmin) throws InterruptedException, FirebaseMessagingException {
        Action action=Action.IDLE;
        if(notificationuser!=null){
            Notification resultadminnotificationuser =  this.save(notificationuser);
            action=resultadminnotificationuser.getAction();
            messagingTemplate.convertAndSendToUser(resultadminnotificationuser.getReciver().getId().toString(),"/queue/user", resultadminnotificationuser);
            Thread.sleep(1000);
            Note note = new Note();
            note.setSubject(action.name());
            note.setContent(this.converMessageenum(action));
            note.setData(notificationuser.toMap());
            if(!this.userDeviceService.findbyuserdevice(notificationuser.getReciver()).isEmpty()){
                List<UserDevice> devices = this.userDeviceService.findbyuserdevice(notificationuser.getReciver());
                for (UserDevice device: devices) {
                    if(device!=null && device.getTokendevice()!=null){
                        if(this.pushNotificationService.isValidFCMToken(device.getTokendevice())){

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


    }
}