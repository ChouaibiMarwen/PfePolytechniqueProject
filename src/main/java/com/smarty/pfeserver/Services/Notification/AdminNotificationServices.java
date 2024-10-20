package com.smarty.pfeserver.Services.Notification;

import com.smarty.pfeserver.Models.Notification.AdminNotification;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Tools.AdminNotificationRepository;
import com.smarty.pfeserver.Response.Notification.AdminNotificationResponse;
import com.smarty.pfeserver.Services.User.UserService;

import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminNotificationServices {
    @Autowired
    private AdminNotificationRepository repository;
    @Autowired
    private UserService userService;
    public AdminNotification save(AdminNotification model){
        return this.repository.save(model);
    }


    public AdminNotificationResponse get_all_notification_admin(int page, int size, Long revicer, Long sender){
        try {
            List<AdminNotification> notificationsList = new ArrayList<AdminNotification>();
            Pageable paging = PageRequest.of(page, size);
            users reciveruser=this.userService.findById(revicer);
            users senderuser=this.userService.findById(sender);
            Page<AdminNotification> pageTuts = this.repository.findAllByReciverAndSenderOrderByTimestmpDesc(paging,reciveruser,senderuser);
            notificationsList = pageTuts.getContent();
            AdminNotificationResponse notificationResponseResponse = new AdminNotificationResponse(
                    notificationsList,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return  notificationResponseResponse;
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("No data found"));
        }

    }
    public AdminNotificationResponse get_all_all_notification_admin(int page, int size, Long reciver){
        try {
            List<AdminNotification> notificationsList = new ArrayList<AdminNotification>();
            Pageable paging = PageRequest.of(page, size);
            users reciveruser=this.userService.findById(reciver);
            Page<AdminNotification> pageTuts = this.repository.findAllByReciverOrderByTimestmpDesc(paging,reciveruser);
            notificationsList = pageTuts.getContent();
            AdminNotificationResponse notificationResponseResponse = new AdminNotificationResponse(
                    notificationsList,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return  notificationResponseResponse;
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("No data found"));
        }

    }
}
