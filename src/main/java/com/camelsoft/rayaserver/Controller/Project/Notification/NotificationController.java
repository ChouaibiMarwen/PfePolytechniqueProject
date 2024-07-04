package com.camelsoft.rayaserver.Controller.Project.Notification;

import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.Project.Ads;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Services.Notification.NotificationServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_ADMIN') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<PaginationResponse> all_my_notification(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PaginationResponse result = this.services.allnotificationbyuser(page, size,user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_my_notification_waiting"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')  or hasRole('SUB_SUPPLIER') or hasRole('SUB_ADMIN') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<PaginationResponse> all_my_notification_waiting(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PaginationResponse result = this.services.allnotificationbyuser(page, size,user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/read_my_waiting_notification"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')  or hasRole('SUB_SUPPLIER') or hasRole('SUB_ADMIN') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<List<Notification>> read_my_waiting_notification (@RequestParam Long[] notificationid) throws IOException {

        List<Notification> result = new ArrayList<>();
        for (Long id: notificationid) {

            if(this.services.existbyid(id)){

                Notification not = this.services.findbyid(id);

                not.setStatus(MessageStatus.READ);

                not = this.services.update(not);

                result.add(not);

            }else {
                return new ResponseEntity("invalid id "  + id, HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/notification_by_id/{notification_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get notification by id", notes = "Endpoint to getget notification by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfull"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Notification> notification_by_id (@PathVariable Long notification_id) {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
         Notification notif = this.services.findbyid(notification_id);
         if(notif == null)
             return new ResponseEntity("invalid id "  + notification_id, HttpStatus.NOT_ACCEPTABLE);
         if(notif.getReciver() !=  user)
             return new ResponseEntity("you are not authorized to get this notification"  + notification_id, HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(notif, HttpStatus.OK);
    }

}
