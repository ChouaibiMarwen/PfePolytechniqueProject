package com.camelsoft.rayaserver.Controller.Project.Notification;

import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Services.Notification.NotificationServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/notifications")
public class NotificationController  extends BaseController {

    @Autowired
    private NotificationServices services;
    @Autowired
    private UserService userService;


    @GetMapping(value = {"/all_my_notification"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('Supplier') or hasRole('SUB_Supplier') or hasRole('SUB_ADMIN')")
    public ResponseEntity<PaginationResponse> all_my_notification(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PaginationResponse result = this.services.allnotificationbyuser(page, size,user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_my_notification_waiting"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('Supplier') ")
    public ResponseEntity<PaginationResponse> all_my_notification_waiting(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PaginationResponse result = this.services.allnotificationbyuser(page, size,user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/read_my_waiting_notification"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('Supplier') ")
    public ResponseEntity<List<Notification>> read_my_waiting_notification (@RequestParam Long[] notificationid) throws IOException {

        List<Notification> result = new ArrayList<>();
        for (Long id: notificationid) {

            if(this.services.existbyid(id)){

                Notification not = this.services.findbyid(id);

                not.setStatus(MessageStatus.READ);

                not = this.services.save(not);

                result.add(not);

            }else {
                return new ResponseEntity("invalid id "  + id, HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
