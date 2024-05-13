package com.camelsoft.rayaserver.Controller.Tools;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Tools.RayaSettings;
import com.camelsoft.rayaserver.Request.Tools.RayaSetiingRequest;
import com.camelsoft.rayaserver.Services.Tools.RayaSettingService;
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
@RequestMapping(value = "/api/v1/raya_settings")
public class RayaSettingController {

    @Autowired
    private RayaSettingService service;

    @PatchMapping(value = {"/raya_settings"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    @ApiOperation(value = "update general settings for admin", notes = "Endpoint to update general settings for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<RayaSettings> UpdateRayaSettings(@ModelAttribute RayaSetiingRequest request) throws IOException {

        RayaSettings settings = this.service.FindFirst();
        if(settings == null)
            settings = new RayaSettings(true,true);


        if(request.getAllowresetpassword() != null){
            settings.setAllowresetpassword(request.getAllowresetpassword());
        }

        if(request.getAllowsignup() != null){
            settings.setAllowsignup(request.getAllowsignup());

        }

        RayaSettings result =  this.service.Update(settings);
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
    public ResponseEntity<RayaSettings> get_settings(@ModelAttribute RayaSetiingRequest request) throws IOException {

        RayaSettings settings = this.service.FindFirst();
        if(settings == null) {
            settings = new RayaSettings(true, true);
            RayaSettings result =  this.service.Update(settings);
            return new ResponseEntity<>(result, HttpStatus.OK);


        }else{
            return new ResponseEntity<>(settings, HttpStatus.OK);

        }

    }

}
