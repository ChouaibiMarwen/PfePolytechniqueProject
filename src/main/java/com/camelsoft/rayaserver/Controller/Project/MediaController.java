package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Request.project.VehiclesMediaRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/media")
public class MediaController {
    private final Log logger = LogFactory.getLog(MediaController.class);
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @DeleteMapping(value = {"/remove_media/{id_file}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('ADMIN') or hasRole('USER')")
    @ApiOperation(value = "remove media", notes = "Endpoint to delete media")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the media id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier ,admin or user")
    })
    public ResponseEntity<String> add_vehicle_media(@PathVariable Long id_file) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        File_model model = this.filesStorageService.findbyid(id_file);
        if (model == null)
            return new ResponseEntity<>("media " + id_file + " not found in the system", HttpStatus.NOT_FOUND);
        this.filesStorageService.delete_file_by_path_from_cdn(model.getUrl(),id_file);

        return new ResponseEntity<>("Media deleted successfully",HttpStatus.OK);
    }


}
