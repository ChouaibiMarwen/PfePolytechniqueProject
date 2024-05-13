package com.camelsoft.rayaserver.Controller.Tools;

import com.camelsoft.rayaserver.Enum.Tools.Language;
import com.camelsoft.rayaserver.Models.Tools.RayaSettings;
import com.camelsoft.rayaserver.Models.Tools.UserConfiguration;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Tools.RayaSetiingRequest;
import com.camelsoft.rayaserver.Request.Tools.UserConfigurationRequest;
import com.camelsoft.rayaserver.Services.Tools.RayaSettingService;
import com.camelsoft.rayaserver.Services.Tools.UserConfigurationService;
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

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/settings")
public class SettingController  extends BaseController {

    @Autowired
    private UserConfigurationService service;
    @Autowired
    private UserService userService;

    @PatchMapping(value = {"/my_settings"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    @ApiOperation(value = "update user settings for admin", notes = "Endpoint to update user settings for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<UserConfiguration> my_settings(@ModelAttribute UserConfigurationRequest request) throws IOException {
        users users = userService.findByUserName(getCurrentUser().getUsername());



        UserConfiguration settings = users.getUserconfiguration();
        if(settings == null)
            settings = new UserConfiguration(true,true, Language.ENGLISH);


        if(request.getNotificationEmail() != null){
            settings.setNotificationemail(request.getNotificationEmail());
        }

        if(request.getNotificationFcm() != null){
            settings.setNotificationfcm(request.getNotificationFcm());

        }

        if(request.getLanguage() != null){
            settings.setLanguage(request.getLanguage());

        }

        UserConfiguration result =  this.service.Update(settings);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @GetMapping(value = {"/settings"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    @ApiOperation(value = "get general settings for admin", notes = "Endpoint to get general settings for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<UserConfiguration> get_settings() throws IOException {
        users users = userService.findByUserName(getCurrentUser().getUsername());



        UserConfiguration settings = users.getUserconfiguration();
        if(settings == null) {
            settings = new UserConfiguration(true,true, Language.ENGLISH);
            UserConfiguration result =  this.service.Update(settings);
            return new ResponseEntity<>(result, HttpStatus.OK);


        }else{
            return new ResponseEntity<>(settings, HttpStatus.OK);

        }

    }
}
