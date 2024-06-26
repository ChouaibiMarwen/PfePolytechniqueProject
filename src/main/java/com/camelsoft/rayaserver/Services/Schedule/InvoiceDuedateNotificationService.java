package com.camelsoft.rayaserver.Services.Schedule;

import com.camelsoft.rayaserver.Enum.Tools.Action;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.Notification.NotificationServices;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private NotificationServices notificationServices;

/*
    public void notifySupplierTowdaysBeforeDueDate() {

        // Calculate today's date
        LocalDate today = LocalDate.now();

        // Calculate the due date (two days from today)
        LocalDate dueDate = today.plusDays(2);

        // Fetch invoices due on the due date
        List<Invoice> invoices = this.invoiceService.findAllByDueDateAndArchiveIsFalse(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        for(Invoice invoice : invoices) {
            Action action=Action.IDLE;
            Notification notificationsupplier = new Notification(null, invoice.getCreatedby(), null, action );
            Notification notificationsubadmin = new Notification(null, invoice.getPurshaseorder().getSubadminassignedto(), null, action );
            try {
                this.notificationServices.sendnotification(notificationsupplier, null);
                this.notificationServices.sendnotification(notificationsubadmin, null);
            } catch (FirebaseMessagingException | InterruptedException ex ) {

            }
        }
    }*/




}
