package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.EventRequest;
import com.camelsoft.rayaserver.Request.project.LoanRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.EventService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/events")
public class EventController {

    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");

    @Autowired
    private EventService service;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;


    @GetMapping(value = {"/all_events_by_title"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all events for admin by name", notes = "Endpoint to get events by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Event>> all_events_by_title(@ModelAttribute String title) throws IOException {
        List<Event> result = this.service.findAllByName(title);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @PostMapping("/add_event")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a new event request from the admin", notes = "Endpoint to add a new event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Event> addEvent(@ModelAttribute EventRequest request, @RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {
        if (request.getTitle() == null || request.getTitle().equals(""))
            return new ResponseEntity("Title can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getEventDate() == null)
            return new ResponseEntity("Event date can't be null", HttpStatus.BAD_REQUEST);
        if (request.getAssignedto() == null || request.getAssignedto().isEmpty())
            return new ResponseEntity("Sent to list can't be null or empty", HttpStatus.BAD_REQUEST);
        File_model resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {
            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia = filesStorageService.save_file(attachment, "events");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        Event  event  = new Event(
                request.getTitle(),
                 request.getDescription(),
                 request.getEventDate(),
                resourceMedia,
                request.getAssignedto()
        );
        Event result = this.service.Save(event);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/event/{id]"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get event by id for admin ", notes = "Endpoint to get event by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Event> geteventById(@PathVariable(required = false) Long id) throws IOException {
        Event result = this.service.FindById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
