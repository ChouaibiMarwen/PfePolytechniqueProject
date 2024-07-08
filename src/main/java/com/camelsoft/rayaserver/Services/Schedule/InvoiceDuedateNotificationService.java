package com.camelsoft.rayaserver.Services.Schedule;

import com.camelsoft.rayaserver.Enum.Tools.Action;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.Notification.NotificationServices;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceDuedateNotificationService {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationServices notificationServices;


    @Scheduled(cron = "0 * * * * ?")
    public void notifySupplierTowdaysBeforeDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate dueDateLocal = today.plusDays(2);

        // Convert LocalDate to Date
        Date dueDate = Date.from(dueDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Invoice> invoices = invoiceService.findAllByDueDateAndArchiveIsFalse(dueDate);
        users user = this.userService.findById(153822L);
        // send notification
        Notification notificationuser = new Notification(
                user,
                user,
                Action.INVOICE,
                "INVOICE_DUE_DATE",
                "Test notification",
                156509L
        );
        try {
            this.notificationServices.sendnotification(notificationuser, null);

        } catch (InterruptedException | FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

      /*  for (Invoice invoice : invoices) {
            Action action = Action.IDLE;
            Notification notificationsupplier = new Notification(null, invoice.getCreatedby(), null, action);
            Notification notificationsubadmin = new Notification(null, invoice.getPurshaseorder().getSubadminassignedto(), null, action);

            try {
                notificationServices.sendnotification(notificationsupplier, null);
                notificationServices.sendnotification(notificationsubadmin, null);
            } catch (FirebaseMessagingException | InterruptedException ex) {
                // Handle the exception, for example log the error
                ex.printStackTrace();
            }*/


        }
    }





