package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Payment;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.PaymentRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.PaymentService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/payment")
public class PaymentsController extends BaseController {
    @Autowired
    private PaymentService service;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService userService;

    @PostMapping(value = {"/add_payment/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "add payments", notes = "Endpoint to add payments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the  id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier ,admin or user")
    })
    public ResponseEntity<Payment> add_payment(@PathVariable Long invoice_id, @ModelAttribute PaymentRequest request, @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        Payment payment = new Payment();
        if (this.invoiceService.FindById(invoice_id) == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Invoice invoice = this.invoiceService.FindById(invoice_id);
        if (invoice.getProducts() != null) {
            Double total = 0.0;
            Double totalpaid = 0.0;
            for (Product p : invoice.getProducts()) {
                total += p.getSubtotal();
            }
            if (invoice.getPayments() != null) {
                for (Payment p : invoice.getPayments()) {
                    totalpaid += p.getAmount();
                }
            }
            if(totalpaid>=total){
                return new ResponseEntity( "this invoice is already paid", HttpStatus.NOT_ACCEPTABLE);

            }
        }else{
            return new ResponseEntity( "this invoice not having any amount (products)", HttpStatus.NOT_ACCEPTABLE);

        }
        if (request.getAmount() == null)
            return new ResponseEntity("you can't pay 0 amount", HttpStatus.NOT_ACCEPTABLE);
        payment.setAmount(request.getAmount());
        payment.setInvoice(invoice);
        if (request.getNotes() != null)
            payment.setNotes(request.getNotes());
        Payment result = this.service.Save(payment);
        if (files != null) {
            List<File_model> filesw = new ArrayList<>();
            for (MultipartFile file : files) {
                File_model resource_media = filesStorageService.save_file_local(file, "payments");
                result.getAttachments().add(resource_media);
                filesw.add(resource_media);
                this.service.Update(result);
            }
        }

        UserAction action = new UserAction(
                UserActionsEnum.PAYMENT_INVOICE,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
