package com.camelsoft.rayaserver.Response.Notification;


import com.camelsoft.rayaserver.Models.Notification.AdminNotification;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminNotificationResponse {
    private List<AdminNotification> adminNotifications = new ArrayList<>();;
    private int currentPage;
    private Long totalItems;
    private int totalPages;

    public AdminNotificationResponse(List<AdminNotification> adminNotifications, int currentPage, Long totalItems, int totalPages) {
        this.adminNotifications = adminNotifications;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }


}
