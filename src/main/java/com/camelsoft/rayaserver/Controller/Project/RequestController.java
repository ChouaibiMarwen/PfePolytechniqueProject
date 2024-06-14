package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.CorrespendanceRequest;
import com.camelsoft.rayaserver.Request.project.RequestsRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.RequestResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.RequestCorrespondenceService;
import com.camelsoft.rayaserver.Services.Project.RequestService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/requests")
public class RequestController  extends BaseController {

    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private RequestService service;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private RequestCorrespondenceService reqcorresservice;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService UserServices;
    @PostMapping(value = {"/add_Request"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Add a new request request from the admin", notes = "Endpoint to add a new request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Request> add_Request(@ModelAttribute RequestsRequest request, @RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Set<Invoice> invoices = new HashSet<>();
        for (Long ids : request.getInvoicesId()) {
            if (!this.invoiceService.ExistById(ids))
                continue;
            Invoice invoice = this.invoiceService.FindById(ids);
            invoices.add(invoice);
        }

        Request requestdata = new Request(
                request.getType(),
                request.getStatus()==null ? RequestState.NONE : request.getStatus(),
                user,
                invoices
        );
        Request result = this.service.Save(requestdata);
        MediaModel resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {
            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia =  filesStorageService.save_file_local(attachment, "requests");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        RequestCorrespondence corssspondences = new RequestCorrespondence();
        corssspondences.setRequest(result);
        corssspondences.setTitle(request.getTitle());
        corssspondences.setDescription(request.getDescription());
        corssspondences.setCreator(user);


        if (resourceMedia != null)
            corssspondences.setAttachment(resourceMedia);
        this.reqcorresservice.Save(corssspondences);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.REQUEST_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping(value = {"/add_correspondance/{requestId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Add a new correspendance  request from the admin", notes = "Endpoint to add a new  correspendance request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<RequestCorrespondence> add_correspondance(@PathVariable Long requestId , @ModelAttribute CorrespendanceRequest request, @RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {

        Request model =  this.service.FindById(requestId);
        if(model == null)
            return new ResponseEntity("this request is not found", HttpStatus.NOT_FOUND);

        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        MediaModel resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {
            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia =  filesStorageService.save_file_local(attachment, "requests");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        RequestCorrespondence corssspondences = new RequestCorrespondence();
        corssspondences.setRequest(model);
        corssspondences.setTitle(request.getTitle());
        corssspondences.setDescription(request.getDescription());
        corssspondences.setCreator(user);


        if (resourceMedia != null)
            corssspondences.setAttachment(resourceMedia);
        RequestCorrespondence result =  this.reqcorresservice.Save(corssspondences);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.REQUEST_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_requests"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all requests by status for supplier and dealer", notes = "Endpoint to get requests for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<RequestResponse> all_requests_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) RequestState status, @RequestParam(required = true) RoleEnum role) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.REQUEST_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        if(role == null)
            return new ResponseEntity("role is required", HttpStatus.BAD_REQUEST);

        if (status != null)
            return new ResponseEntity<>(this.service.findAllByStateAndRole(page, size, status, role), HttpStatus.OK);

        return new ResponseEntity<>(this.service.findAllByRole(page, size, role), HttpStatus.OK);

    }


    @GetMapping(value = {"/my_requests"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all requests by status for current_user", notes = "Endpoint to get requests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<DynamicResponse> my_requests(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) RequestState status) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.REQUEST_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(this.service.findAllByCreatedByAndStatus(page, size, user, status), HttpStatus.OK);

    }

    @GetMapping(value = {"/request/{idRequest}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all requests by status for admin", notes = "Endpoint to get requests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Request> all_requests_admin(@PathVariable Long idRequest) throws IOException {

        boolean exist = this.service.ExistById(idRequest);
        if(!exist)
            return new ResponseEntity("wrong id request: id not found", HttpStatus.BAD_REQUEST);

        Request req = this.service.FindById(idRequest);
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.REQUEST_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(req, HttpStatus.OK);


    }
@GetMapping(value = {"/corssspondences/{idRequest}"})
@PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all requests by status for admin", notes = "Endpoint to get requests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Set<RequestCorrespondence>> all_corssspondences_request(@PathVariable Long idRequest) throws IOException {

        boolean exist = this.service.ExistById(idRequest);
        if(!exist)
            return new ResponseEntity("wrong id request: id not found", HttpStatus.BAD_REQUEST);

        Request req = this.service.FindById(idRequest);
    // Get the set of RequestCorrespondence entities ordered by timestamp in descending order
    Set<RequestCorrespondence> correspondences = req.getCorssspondences()
            .stream()
            .sorted(Comparator.comparing(RequestCorrespondence::getTimestamp).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
             users user = UserServices.findByUserName(getCurrentUser().getUsername());
                 //save new action
             UserAction action = new UserAction(
            UserActionsEnum.REQUEST_MANAGEMENT,
            user
            );
         this.userActionService.Save(action);
        return new ResponseEntity<>(correspondences, HttpStatus.OK);

    }









}
