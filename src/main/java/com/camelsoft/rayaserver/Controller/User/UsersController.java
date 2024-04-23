package com.camelsoft.rayaserver.Controller.User;

 

import com.camelsoft.rayaserver.Enum.User.SessionAction;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.User.LogOutRequest;
import com.camelsoft.rayaserver.Request.User.UpdateUserRequest;
import com.camelsoft.rayaserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.camelsoft.rayaserver.Response.Tools.ApiResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;

import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.User.UserSessionService;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Tools.Exception.UserLogoutException;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/users")
public class UsersController extends BaseController {
    private final Log logger = LogFactory.getLog(UsersController.class);
    private static List<String> image_accepte_type = Arrays.asList("PNG", "png", "jpeg", "JPEG", "JPG", "jpg");
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_profile_user"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<users> update_profile_user(@ModelAttribute UpdateUserRequest request, @RequestParam(required = false, name = "file") MultipartFile file) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        File_model imagepath = user.getImage();
        if (file != null) {
            String extention = file.getContentType().substring(file.getContentType().indexOf("/") + 1);
            if (!image_accepte_type.contains(extention))
                return new ResponseEntity("this type is not acceptable : " + extention, HttpStatus.BAD_REQUEST);
            File_model resource_media = filesStorageService.save_file(file, "profile");
            if (user.getImage() != null)
                this.filesStorageService.delete_file_by_path_from_cdn(user.getImage().getUrl(),user.getImage().getId());

            if (resource_media == null)
                return new ResponseEntity("image not valid", HttpStatus.BAD_REQUEST);
            imagepath = resource_media;
        }

        if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
        if (request.getLastName() != null) user.setLastname(request.getLastName());
        user.setName(user.getFirstname() + " " + user.getLastname());
        if (request.getPhonenumber() != null) user.setPhonenumber(request.getPhonenumber());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getCity() != null) user.setCity(request.getCity());
        user.setNotificationFcm(request.isNotificationfcm());
        user.setNotificationEmail(request.isNotificationemail());
        user.setImage(imagepath);

        users result = userService.UpdateUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/logout")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
    public ResponseEntity<ApiResponse> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        if (deviceId == null)
            return new ResponseEntity("device id not found", HttpStatus.BAD_REQUEST);

        if (!this.userDeviceService.existbytoken(logOutRequest.getToken()))
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);

        if (this.userDeviceService.findbytoken(logOutRequest.getToken()) == null)
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);


        UserDevice userDevice = this.userDeviceService.findbytoken(logOutRequest.getToken());
        if (userDevice != null && userDevice.getDeviceId().equals(deviceId)) {
            OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(), logOutRequest.getToken(), logOutRequest);
            applicationEventPublisher.publishEvent(logoutSuccessEvent);
            userDevice.setLogout(true);
            UserDevice deviceResult = this.userDeviceService.update(userDevice);
            UserSession session = new UserSession(currentUser,deviceResult, SessionAction.LOGOUT,"users Logout ");
            this.userSessionService.save(session);
            return ResponseEntity.ok(new ApiResponse(true, "users has successfully logged out from the system!"));
        } else {
            throw new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user ");
        }

    }

    @PatchMapping(value = {"/update_password"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity update_password(@RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword) throws IOException {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(currentUser.getUsername(), oldpassword)
        );
        if (!authentication.isAuthenticated())
            return new ResponseEntity("password mismatch", HttpStatus.CONFLICT);
        this.userService.updatepassword(currentUser, newpassword);
        return new ResponseEntity("password updated successfully", HttpStatus.OK);

    }


    @GetMapping(value = {"/get_user/{userid}"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<users> get_user_by_id(@PathVariable Long userid) throws IOException {
        if (!this.userService.existbyid(userid))
            return new ResponseEntity("user not exist", HttpStatus.NOT_FOUND);
        users result = this.userService.findById(userid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
