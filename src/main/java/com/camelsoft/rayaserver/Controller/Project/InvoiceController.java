package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.InvoiceRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.ProductService;
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

import java.io.IOException;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/invoice")
public class InvoiceController extends BaseController {
    private final Log logger = LogFactory.getLog(InvoiceController.class);

    @Autowired
    private InvoiceService service;
    @Autowired
    private ProductService productservice;
    @Autowired
    private UserService UserServices;

    @GetMapping(value = {"/all_invoice"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) InvoiceStatus status, @RequestParam(required = true) InvoiceRelated related) throws IOException {
        if (related == InvoiceRelated.NONE)
            return new ResponseEntity("you need to choose related NONE not a related", HttpStatus.NOT_ACCEPTABLE);
        if (status != null)
            return new ResponseEntity<>(this.service.FindAllPg(page, size, related), HttpStatus.OK);

        return new ResponseEntity<>(this.service.FindAllByState(page, size, status, related), HttpStatus.OK);


    }

    @PostMapping(value = {"/add_invoice"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    @ApiOperation(value = "add invoice for admin  and supplier", notes = "Endpoint to add invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> add_invoice(@ModelAttribute InvoiceRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        if (this.service.ExistByInvoiceNumber(request.getInvoicenumber())) {
            return new ResponseEntity(request.getInvoicenumber() + "is already found , please try something else !", HttpStatus.FOUND);
        }
        if (request.getRelated() == null || request.getRelated() == InvoiceRelated.NONE) {
            return new ResponseEntity("you need to defined the invoice relation " + request.getRelated(), HttpStatus.NOT_ACCEPTABLE);
        }
        Set<Product> products = this.productservice.SaveProductList(request.getProducts());
        Invoice invoice = new Invoice(
                request.getInvoicenumber(),
                request.getInvoicedate(),
                request.getDuedate(),
                request.getCurrency(),
                request.getSuppliername(),
                request.getSupplierzipcode(),
                request.getSupplierstreetadress(),
                request.getSupplierphonenumber(),
                request.getBankname(),
                request.getBankzipcode(),
                request.getBankstreetadress(),
                request.getBankphonenumber(),
                request.getVehicleregistration(),
                request.getVehiclecolor(),
                request.getVehiclevin(),
                request.getVehiclemodel(),
                request.getVehiclemark(),
                request.getVehiclemileage(),
                request.getVehiclemotexpiry(),
                request.getVehicleenginesize(),
                products,
                user,
                request.getRelated()
        );
        Invoice result = this.service.Save(invoice);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @PatchMapping(value = {"/update_invoice/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    @ApiOperation(value = "update invoice for admin and supplier", notes = "Endpoint to update invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Invoice> update_invoice(@PathVariable Long invoice_id, @ModelAttribute String remark, @ModelAttribute InvoiceStatus status) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (this.service.FindById(invoice_id) == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Invoice invoice = this.service.FindById(invoice_id);
        if (remark != null) invoice.setRemark(remark);
        if (status != null) invoice.setStatus(status);
        Invoice result = this.service.Update(invoice);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

}
