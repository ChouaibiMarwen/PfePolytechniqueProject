package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.Ads;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.AdsRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.AdsService;
import com.camelsoft.rayaserver.Services.User.UserActionService;
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

    @Autowired
    private UserActionService userActionService;



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
        ads.setAddtoslider(request.getAddtoslider());

        //save attachmen't files and set it to the new ads
        if (this.filesStorageService.checkformatArrayList(request.getAttachments())) {
            Set<MediaModel> adsAttchments = new HashSet<>();
            adsAttchments = filesStorageService.save_all(request.getAttachments(), "vehicles");
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


        UserAction action = new UserAction(
                UserActionsEnum.ADS_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
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
        if(request.getAddtoslider() != null)
            ads.setAddtoslider(request.getAddtoslider());
        if(request.getDescription() != null && !request.getDescription().isEmpty())
            ads.setDescription(request.getDescription());
        if(request.getAttachments() != null && !request.getAttachments().isEmpty()){
            if (this.filesStorageService.checkformatArrayList(request.getAttachments())) {
                Set<MediaModel> adsAttchments = new HashSet<>();
                adsAttchments = filesStorageService.save_all(request.getAttachments(), "vehicles");
                if (adsAttchments == null || adsAttchments.isEmpty()) {
                    return new ResponseEntity("can't upload the attachment file", HttpStatus.BAD_REQUEST);
                }
                ads.setAttachments(adsAttchments);
            }else {
                return new ResponseEntity("not accepted file type", HttpStatus.BAD_REQUEST);
            }
        }
        ads = this.adsService.update(ads);
        UserAction action = new UserAction(
                UserActionsEnum.ADS_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(ads, HttpStatus.OK);

    }
    @GetMapping(value = {"/get_all_ads"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all ads list from the admin", notes = "Endpoint to get all ads list for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update the ads"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<Ads>> get_all_ads () {
        List<Ads> result = this.adsService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/ads_by_id/{idAds}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all ads list from the admin", notes = "Endpoint to get all ads list for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update the ads"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Ads> get_all_ads (@PathVariable Long idAds) {
        Ads ads = this.adsService.findbyid(idAds);
        if(ads == null)
            return new ResponseEntity("no ads founded with this is: " + idAds, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ads, HttpStatus.OK);
    }


    @DeleteMapping(value = {"/delete_ads/{id}"})
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ADS_WRITE')")
    public ResponseEntity<String> delete_ads (@PathVariable Long id) throws IOException {

        Ads ads = this.adsService.findbyid(id);
        if(ads == null)
            return new ResponseEntity("no ads founded with this is: " + id, HttpStatus.NOT_FOUND);
        ads.getAttachments().clear();
        this.adsService.deletebyid(id);

        //get current user to savev action
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>("Deleted !", HttpStatus.OK);
    }

}
