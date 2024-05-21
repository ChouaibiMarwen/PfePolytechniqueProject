package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.VehiclesMediaRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/media")
public class MediaController extends BaseController {
    private final Log logger = LogFactory.getLog(MediaController.class);
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService userService;

    @DeleteMapping(value = {"/remove_media/{id_file}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "remove media", notes = "Endpoint to delete media")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the media id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier ,admin or user")
    })
    public ResponseEntity<String> remove_media(@PathVariable Long id_file) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        File_model model = this.filesStorageService.findbyid(id_file);
        if (model == null)
            return new ResponseEntity<>("media " + id_file + " not found in the system", HttpStatus.NOT_FOUND);
        this.filesStorageService.delete_file_by_path_from_cdn(model.getUrl(), id_file);

        return new ResponseEntity<>("Media deleted successfully", HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_profile_image"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "update current user profile image", notes = "Endpoint to update profile image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad Request, the media is Null"),
            @ApiResponse(code = 406, message = "Not Acceptable, the type is not acceptable"),
            @ApiResponse(code = 501, message = "Not Implemented, please try again")
    })
    public ResponseEntity<users> update_profile_image(@RequestParam(required = false, name = "file") MultipartFile file) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        File_model oldimage = user.getProfileimage();
        if (file == null) {
            return new ResponseEntity("file is null", HttpStatus.BAD_REQUEST);
        }
        if (!this.filesStorageService.checkformat(file))
            return new ResponseEntity("this type is not acceptable : ", HttpStatus.NOT_ACCEPTABLE);
        File_model resource_media = filesStorageService.save_file_local(file, "profile");
        if (resource_media == null)
            return new ResponseEntity("error saving file", HttpStatus.NOT_IMPLEMENTED);
        user.setProfileimage(resource_media);
        users result = userService.UpdateUser(user);
        if (oldimage != null)
            this.filesStorageService.delete_file_by_path_from_cdn(oldimage.getUrl(), oldimage.getId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
