package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Request;
import com.camelsoft.rayaserver.Models.Project.RequestCorrespondence;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Project.RequestRepository;
import com.camelsoft.rayaserver.Request.project.EventRequest;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
import com.camelsoft.rayaserver.Request.project.RequestsRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.EventService;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.RequestService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/requests")
public class RequestController extends BaseController {

    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");


    @Autowired
    private RequestService service;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService UserServices;


    @PostMapping(value = {"/add_Request"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a new request request from the admin", notes = "Endpoint to add a new request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Request> addEvent(@ModelAttribute RequestsRequest request) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Set<Invoice> invoices = new HashSet<>();
        for (Long ids : request.getInvoicesId()) {
            if (!this.invoiceService.ExistById(ids))
                continue;
            Invoice invoice = this.invoiceService.FindById(ids);
            invoices.add(invoice);
        }

        Request requestdata = new Request(
                request.getTitle(),
                request.getStatus(),
                user,
                invoices
        );
        Request result = this.service.Save(requestdata);
        File_model resourceMedia = null;
        if (request.getCorrespondant().getAttachment() != null && !request.getCorrespondant().getAttachment().isEmpty()) {
            String extension = request.getCorrespondant().getAttachment().getContentType().substring(request.getCorrespondant().getAttachment().getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia = filesStorageService.save_file_local(request.getCorrespondant().getAttachment(), "requests");
        }
        RequestCorrespondence corssspondences = new RequestCorrespondence();
        corssspondences.setRequest(result);
        corssspondences.setTitle(request.getTitle());
        corssspondences.setDescription(request.getDescription());
        if (resourceMedia != null)
            corssspondences.setAttachment(resourceMedia);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
