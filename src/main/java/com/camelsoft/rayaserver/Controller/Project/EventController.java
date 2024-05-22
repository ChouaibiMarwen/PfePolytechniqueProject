package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.DTO.PurchaseOrderDto;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.EventRequest;
import com.camelsoft.rayaserver.Request.project.LoanRequest;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.EventService;
import com.camelsoft.rayaserver.Services.User.RoleService;
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
@RequestMapping(value = "/api/v1/events")
public class EventController {

    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");

    @Autowired
    private EventService service;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;


    @GetMapping(value = {"/all_events_by_title"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
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

    @GetMapping(value = {"/all_events_by_title_and_status_paginated"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all events for admin by title paginated ", notes = "Endpoint to get events by name and character paginated ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_events_by_title_paginated(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) String  title, @RequestParam(required = false) EventStatus  status) throws IOException {

         return new ResponseEntity<>(this.service.findAllByTtileOrStatusPaginated(page, size , title, status), HttpStatus.OK);

    }

    @GetMapping(value = {"/all_events_by_title_and_date_paginated"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all events for admin by title and date paginated ", notes = "Endpoint to get events by name character and date paginated ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_events_by_title__and_date_paginated(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) String  title, @RequestParam(required = false) Date  creationdate) throws IOException {

        return new ResponseEntity<>(this.service.findAllByTtileOrDatePaginated(page, size , title, creationdate), HttpStatus.OK);

    }


    @GetMapping(value = {"/all_events_by_role_assignedto"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all events  by assignedto role paginated ", notes = "Endpoint to get events by  by assignedto role paginated ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_events_by_role_assignedto(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam RoleEnum role) throws IOException {

        return new ResponseEntity<>(this.service.FindEventAssignedToByRole(page, size ,role), HttpStatus.OK);

    }



    @PostMapping(value = {"/add_event"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new event request from the admin", notes = "Endpoint to add a new event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Event> addEvent(@ModelAttribute RequestOfEvents request,@RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {
        if (request.getTitle() == null)
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
            resourceMedia = filesStorageService.save_file_local(attachment, "events");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        Event  event  = new Event(
                request.getTitle(),
                request.getDescription(),
                request.getEventDate(),
                resourceMedia,
                request.getAssignedto(),
                request.getStatus()
        );
        Event result = this.service.Save(event);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/event/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get event by id for admin ", notes = "Endpoint to get event by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Event> geteventById(@PathVariable Long id) throws IOException {
        Event result = this.service.FindById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = {"/inverse_status_event/{event_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "inverse status of event  for admin ", notes = "Endpoint update status event for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Event> update_status_event(@PathVariable Long event_id) throws IOException {
        if (this.service.FindById(event_id)== null) {
            return new ResponseEntity(event_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Event event = this.service.FindById(event_id);
        if (event.getStatus() == EventStatus.PUBLISHED) {
            event.setStatus(EventStatus.DRAFT);
        } else if (event.getStatus() == EventStatus.DRAFT) {
            event.setStatus(EventStatus.PUBLISHED);
        }
        Event result = this.service.Update(event);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }



    @PatchMapping(value = {"/update_event/{idEvent}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "update an event from the admin", notes = "Endpoint to update an event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Event> updateevnt( @PathVariable Long idEvent,  @ModelAttribute RequestOfEvents request,@RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {

        boolean exist = this.service.ExistById(idEvent);
        if(!exist)
            return new ResponseEntity("envent by this id :" + idEvent + "is not founded ", HttpStatus.NOT_ACCEPTABLE);

        Event event =  this.service.FindById(idEvent);

        File_model resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {

            if(event.getAttachment() != null){
                File_model model = this.filesStorageService.findbyid(event.getAttachment().getId());
                /*if (model == null)
                    return new ResponseEntity("media si not found in the system", HttpStatus.NOT_FOUND);*/
                this.filesStorageService.delete_file_by_path_from_cdn(model.getUrl(), event.getAttachment().getId());
            }

            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia = filesStorageService.save_file_local(attachment, "events");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        if (request.getAssignedto() != null) {
            event.setAssignedto(request.getAssignedto());
        }
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (resourceMedia != null) {
            event.setAttachment(resourceMedia);
        }
        Event result = this.service.Update(event);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    @DeleteMapping(value = {"/{event_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<Event> daleteEvent(@PathVariable Long event_id){
        boolean exist = this.service.ExistById(event_id);
        if(!exist)
            return new ResponseEntity("envent by this id :" + event_id + "is not founded ", HttpStatus.NOT_ACCEPTABLE);

        Event event =  this.service.FindById(event_id);
        event.setArchive(true);
        event = this.service.Update(event);
        return new ResponseEntity<>(event, HttpStatus.OK);

    }




}
