package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Ads;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.AdsRequest;
import com.camelsoft.rayaserver.Services.File.FileServices;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.AdsService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/ads")
public class AdsController extends BaseController {

    @Autowired
    private UserService userService;


    @Autowired
    private AdsService adsService;

    @Autowired
    private FilesStorageServiceImpl filesStorageService;



    @PostMapping(value = {"/add_pub"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new pub from the admin", notes = "Endpoint to add a new ads for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added ads"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Ads> addAds(@ModelAttribute AdsRequest request ) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (request.getUrl() == null || request.getUrl().isEmpty())
            return new ResponseEntity("Pub url can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getAttachments()== null || request.getAttachments().isEmpty())
            return new ResponseEntity("Pub url can't be null or empty", HttpStatus.BAD_REQUEST);
        Ads ads = new Ads();
        ads.setUrl(request.getUrl());

        //save attachmen't files and set it to the new ads
        if (this.filesStorageService.checkformatArrayList(request.getAttachments())) {
            Set<File_model> adsAttchments = new HashSet<>();
            adsAttchments = filesStorageService.save_all_local(request.getAttachments(), "vehicles");
            if (adsAttchments == null || adsAttchments.isEmpty()) {
                return new ResponseEntity("can't upload the attachment file", HttpStatus.BAD_REQUEST);
            }
            ads.setAttachments(adsAttchments);
        }else {
            return new ResponseEntity("not accepted file type", HttpStatus.BAD_REQUEST);
        }

        //description attribute is not require, so if exist set it to the new ads
        if(request.getDescription()!=null && !request.getDescription().isEmpty())
            ads.setDescription(request.getDescription());

        ads = this.adsService.save(ads);
        return new ResponseEntity<>(ads, HttpStatus.OK);

    }


    @PatchMapping(value = {"/update_pub/{idAds}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Update a new ads from the admin", notes = "Endpoint to update a new ads for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update the ads"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Ads> updateAds(@PathVariable Long idAds, @ModelAttribute AdsRequest request ) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);

        Ads ads =  this.adsService.findbyid(idAds);
        if (ads == null)
            return new ResponseEntity("no ads founded with this is: " + idAds, HttpStatus.NOT_FOUND);
        if(request.getUrl() != null && !request.getUrl().isEmpty())
            ads.setUrl(request.getUrl());
        if(request.getDescription() != null && !request.getDescription().isEmpty())
            ads.setDescription(request.getDescription());
        if(request.getAttachments() != null && !request.getAttachments().isEmpty()){
            if (this.filesStorageService.checkformatArrayList(request.getAttachments())) {
                Set<File_model> adsAttchments = new HashSet<>();
                adsAttchments = filesStorageService.save_all_local(request.getAttachments(), "vehicles");
                if (adsAttchments == null || adsAttchments.isEmpty()) {
                    return new ResponseEntity("can't upload the attachment file", HttpStatus.BAD_REQUEST);
                }
                ads.setAttachments(adsAttchments);
            }else {
                return new ResponseEntity("not accepted file type", HttpStatus.BAD_REQUEST);
            }
        }

        ads = this.adsService.update(ads);
        return new ResponseEntity<>(ads, HttpStatus.OK);

    }




}
