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

    // this service is scheduled every day at midnight(00:00)
    @Scheduled(cron = "0 0 0 * * ?")
    public void notifySupplierTowdaysBeforeDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate dueDateLocal = today.plusDays(2);

        // Convert LocalDate to Date
        Date dueDate = Date.from(dueDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Invoice> invoices = invoiceService.findAllByDueDateAndArchiveIsFalse(dueDate);
        if (!invoices.isEmpty()) {
            users admin = this.userService.findFirstAdmin();
            if (admin == null)
                return;

            for (Invoice invoice : invoices) {
                if (invoice.getPurshaseorder().getSubadminassignedto() != null) {
                    users subadmin = invoice.getPurshaseorder().getSubadminassignedto();
                    Notification notificationuser = new Notification(
                            admin,
                            subadmin,
                            Action.INVOICE,
                            "INVOICE_DUE_DATE",
                            "Due of invoice with id " + invoice.getId() + " is after tow days",
                            invoice.getId()
                    );

                    // send notification to sub admin and admin
                    try {
                        this.notificationServices.sendnotification(notificationuser, notificationuser);

                    } catch (InterruptedException | FirebaseMessagingException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                    Notification notificationuser = new Notification(
                            admin,
                            admin,
                            Action.INVOICE,
                            "INVOICE_DUE_DATE",
                            "Due of invoice with id " + invoice.getId() + " is after tow days",
                            invoice.getId()
                    );

                    // send notification to tha admin as user
                    try {
                        this.notificationServices.sendnotification(notificationuser, null);

                    } catch (InterruptedException | FirebaseMessagingException e) {
                        throw new RuntimeException(e);
                    }

                }

            }


        }
    }
}





