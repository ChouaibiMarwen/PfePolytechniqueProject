package com.camelsoft.rayaserver.Controller.User;


import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.SessionAction;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.User.LogOutRequest;
import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;

import com.camelsoft.rayaserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.camelsoft.rayaserver.Response.Tools.ApiResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;

import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
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
import java.util.Date;
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
    @Autowired
    private PersonalInformationService personalInformationService;

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_personal_information"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<PersonalInformation> update_personal_information(@ModelAttribute PersonalInformationRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PersonalInformation personalInformation = new PersonalInformation();
        if(user.getPersonalinformation()!=null) {
            personalInformation = user.getPersonalinformation();
        }else{
            personalInformation = this.personalInformationService.save(personalInformation);
            user.setPersonalinformation(personalInformation);
            this.userService.UpdateUser(user);
        }
        if (request.getFirstnameen() != null) personalInformation.setFirstnameen(request.getFirstnameen());
        if (request.getLastnameen() != null) personalInformation.setLastnameen(request.getLastnameen());
        if (request.getFirstnamear() != null) personalInformation.setFirstnamear(request.getFirstnamear());
        if (request.getLastnamear() != null) personalInformation.setLastnamear(request.getLastnamear());
        if (request.getBirthDate() != null) personalInformation.setBirthDate(request.getBirthDate());
        if (request.getSecondnamear() != null) personalInformation.setSecondnamear(request.getSecondnamear());
        if (request.getThirdnamear() != null) personalInformation.setThirdnamear(request.getThirdnamear());
        if (request.getGrandfathernamear() != null) personalInformation.setGrandfathernamear(request.getGrandfathernamear());
        if (request.getSecondnameen() != null) personalInformation.setSecondnameen(request.getSecondnameen());
        if (request.getThirdnameen() != null) personalInformation.setThirdnameen(request.getThirdnameen());
        if (request.getGrandfathernameen() != null) personalInformation.setGrandfathernameen(request.getGrandfathernameen());
        if (request.getNumberofdependents() != null) personalInformation.setNumberofdependents(request.getNumberofdependents());
        if (request.getGender() != null) personalInformation.setGender(request.getGender());
        if (request.getWorksector() != null) personalInformation.setWorksector(request.getWorksector());
        if (request.getMaritalstatus() != null) personalInformation.setMaritalstatus(request.getMaritalstatus());
        PersonalInformation result = this.personalInformationService.update(personalInformation);



        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_profile_image"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<users> update_profile_image(@RequestParam(required = false, name = "file") MultipartFile file) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        File_model imagepath = user.getProfileimage();
        if (file != null) {
            String extention = file.getContentType().substring(file.getContentType().indexOf("/") + 1);
            if (!image_accepte_type.contains(extention))
                return new ResponseEntity("this type is not acceptable : " + extention, HttpStatus.BAD_REQUEST);
            File_model resource_media = filesStorageService.save_file(file, "profile");
            if (user.getProfileimage() != null)
                this.filesStorageService.delete_file_by_path_from_cdn(user.getProfileimage().getUrl(), user.getProfileimage().getId());

            if (resource_media == null)
                return new ResponseEntity("image not valid", HttpStatus.BAD_REQUEST);
            imagepath = resource_media;
        }
        user.setProfileimage(imagepath);
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
            UserSession session = new UserSession(currentUser, deviceResult, SessionAction.LOGOUT, "users Logout ");
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
