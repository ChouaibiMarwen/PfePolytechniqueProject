package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.AvailiabilityEnum;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.InvoiceRepportRequest;
import com.camelsoft.rayaserver.Request.project.InvoiceRequest;
import com.camelsoft.rayaserver.Request.project.RefundInvoiceRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.InvoiceReport;
import com.camelsoft.rayaserver.Services.Project.*;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/invoice")
public class InvoiceController extends BaseController {
    private final Log logger = LogFactory.getLog(InvoiceController.class);

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private InvoiceService service;
    @Autowired
    private ProductService productservice;
    @Autowired
    private UserService UserServices;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private RefundInvoiceService refundInvoiceService;
    @Autowired
    private PurshaseOrderService purshaseOrderService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private VehiclesService vehiclesService;

    @GetMapping(value = {"/all_invoice_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_invoice_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) InvoiceStatus status, @RequestParam List<RoleEnum> role) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        Page<Invoice> invoice = this.criteriaService.findAllByStatusAndRole(page, size, status, role);
        DynamicResponse res = new DynamicResponse(invoice.getContent(), invoice.getNumber(), invoice.getTotalElements(), invoice.getTotalPages());

        return new ResponseEntity<>(res, HttpStatus.OK);


    }

    @GetMapping(value = {"/all_invoice_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all invoice by status for supplier", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_invoice_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) InvoiceStatus status, @RequestParam(required = false) InvoiceRelated related, @RequestParam(required = false) Boolean thirdparty, @RequestParam(required = false) Integer invoicenumber) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        Page<Invoice> invoice = this.criteriaService.findAllByStatusAndRelatedAndUsers(page, size, status, related, user, thirdparty, invoicenumber);
        DynamicResponse res = new DynamicResponse(invoice.getContent(), invoice.getNumber(), invoice.getTotalElements(), invoice.getTotalPages());

        // return new ResponseEntity<>(this.service.FindAllPg(page, size, related), HttpStatus.OK);
        return new ResponseEntity<>(res, HttpStatus.OK);


    }

    @PostMapping(value = {"/add_invoice"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add invoice for admin  and supplier", notes = "Endpoint to add invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> add_invoice(@RequestBody(required = false) InvoiceRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (request.getVehiclevin() == null)
            return new ResponseEntity("VIN should be not null", HttpStatus.NOT_ACCEPTABLE);

        Vehicles vehicles = this.vehiclesService.FindByVIN(request.getVehiclevin());
        if (vehicles == null)
            return new ResponseEntity(request.getVehiclevin() + "this vehicle VIN  not found", HttpStatus.NOT_ACCEPTABLE);
        users createdby = UserServices.findByUserName(getCurrentUser().getUsername());
        users relatedto = UserServices.findById(request.getRelatedtouserid());

        if (request.getInvoicenumber() == null || this.service.ExistByInvoiceNumber(request.getInvoicenumber())) {
            return new ResponseEntity(request.getInvoicenumber() + "is already found , please try something else !", HttpStatus.FOUND);
        }
        if (request.getRelated() == null || request.getRelated() == InvoiceRelated.NONE) {
            return new ResponseEntity("you need to defined the invoice relation " + request.getRelated(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (relatedto.getRole().getRole() != RoleEnum.ROLE_ADMIN && relatedto.getRole().getRole() != RoleEnum.ROLE_SUB_ADMIN) {
            if (relatedto.getSupplier() != null) {
                if (request.getRelated() != InvoiceRelated.SUPPLIER && request.getRelated() != InvoiceRelated.SUB_DEALER)
                    return new ResponseEntity("the related to is a supplier or sub-dealer and the related is not a supplier or sub-dealer", HttpStatus.NOT_ACCEPTABLE);

            } else {
                if (request.getRelated() != InvoiceRelated.CUSTOMER)
                    return new ResponseEntity("the related to is a customer and the related is not a customer", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        Set<Product> products = this.productservice.GetProductList(request.getProducts());
        Invoice invoice = new Invoice();
        if (request.getThirdpartypoid() != null) {
            if (this.service.existByThirdpartypoid(request.getThirdpartypoid()))
                return new ResponseEntity(request.getThirdpartypoid() + "is already found , please try something else !", HttpStatus.FOUND);

            invoice.setThirdpartypoid(request.getThirdpartypoid());
        }
        if (request.getInvoicenumber() != null) {
            invoice.setInvoicenumber(request.getInvoicenumber());
        }
        if (request.getInvoicedate() != null) {
            invoice.setInvoicedate(request.getInvoicedate());

        }
        if (request.getDuedate() != null) {
            invoice.setDuedate(request.getDuedate());

        }
        if (request.getCurrency() != null) {
            invoice.setCurrency(request.getCurrency());

        }
        if (request.getSuppliername() != null) {
            invoice.setSuppliername(request.getSuppliername());

        }
        if (request.getSupplierzipcode() != null) {
            invoice.setSupplierzipcode(request.getSupplierzipcode());

        }
        if (request.getSupplierstreetadress() != null) {
            invoice.setSupplierstreetadress(request.getSupplierstreetadress());

        }
        if (request.getSupplierphonenumber() != null) {
            invoice.setSupplierphonenumber(request.getSupplierphonenumber());

        }
        if (request.getBankname() != null) {
            invoice.setBankname(request.getBankname());

        }
        if (request.getBankzipcode() != null) {
            invoice.setBankzipcode(request.getBankzipcode());

        }
        if (request.getBankstreetadress() != null) {
            invoice.setBankstreetadress(request.getBankstreetadress());

        }
        if (request.getBankphonenumber() != null) {
            invoice.setBankphonenumber(request.getBankphonenumber());

        }
        if (request.getVehicleregistration() != null) {
            invoice.setVehicleregistration(request.getVehicleregistration());

        }
        if (request.getVehiclecolor() != null) {
            invoice.setVehiclecolor(request.getVehiclecolor());

        }
        if (request.getVehiclevin() != null) {
            invoice.setVehiclevin(request.getVehiclevin());

        }
        if (request.getVehiclemodel() != null) {
            invoice.setVehiclemodel(request.getVehiclemodel());

        }
        if (request.getVehiclemark() != null) {
            invoice.setVehiclemark(request.getVehiclemark());

        }
        if (request.getVehiclemileage() != null) {
            invoice.setVehiclemileage(request.getVehiclemileage());

        }
        if (request.getVehiclemotexpiry() != null) {
            invoice.setVehiclemotexpiry(request.getVehiclemotexpiry());

        }

        if (request.getVehicleenginesize() != null) {
            invoice.setVehicleenginesize(request.getVehicleenginesize());

        }
        if (products != null) {
            invoice.setProducts(products);

        }
        if (createdby != null) {
            invoice.setCreatedby(createdby);

        }
        if (request.getRelated() != null) {
            invoice.setRelated(request.getRelated());

        }
        if (relatedto != null) {
            invoice.setRelatedto(relatedto);

        }
        if (request.getBankiban() != null) {
            invoice.setBankiban(request.getBankiban());

        }
        if (request.getBankrip() != null) {
            invoice.setBankrib(request.getBankrip());

        }

        if (request.getVehicleprice() != null) {
            invoice.setVehicleprice(request.getVehicleprice());
        } else if (vehicles.getVehiclespricefinancing() != null) {
            invoice.setVehicleprice(vehicles.getVehiclespricefinancing().getTotalamount());
        }
        Invoice result = this.service.Save(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @PostMapping(value = {"/add_invoice/{poid}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add invoice for admin  and supplier", notes = "Endpoint to add invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> add_invoice_for_po(@RequestBody(required = false) InvoiceRequest request, @PathVariable Long poid) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        PurshaseOrder po = purshaseOrderService.FindById(poid);
        if (po == null)
            return new ResponseEntity("this po not found", HttpStatus.NOT_FOUND);
        if (po.getInvoice() != null)
            return new ResponseEntity("this po already have invoice", HttpStatus.NOT_ACCEPTABLE);
        if (request.getVehiclevin() == null)
            return new ResponseEntity("VIN should be not null", HttpStatus.NOT_ACCEPTABLE);
        Vehicles vehicles = this.vehiclesService.FindByVIN(request.getVehiclevin());
        if (vehicles == null)
            return new ResponseEntity(request.getVehiclevin() + "this vehicle VIN  not found", HttpStatus.NOT_ACCEPTABLE);
        users createdby = user;
        users relatedto = null;

        if (request.getInvoicenumber() == null || this.service.ExistByInvoiceNumber(request.getInvoicenumber())) {
            return new ResponseEntity(request.getInvoicenumber() + "is already found , please try something else !", HttpStatus.FOUND);
        }
        if (user.getRole().getRole() != RoleEnum.ROLE_ADMIN && user.getRole().getRole() != RoleEnum.ROLE_SUB_ADMIN) {
            request.setRelated(InvoiceRelated.RAYAFINANCING);
            request.setRelatedtouserid(user.getId());
            relatedto = user;
        } else {
            if (request.getRelated() == null || request.getRelated() == InvoiceRelated.NONE) {
                return new ResponseEntity("you need to defined the invoice relation " + request.getRelated(), HttpStatus.NOT_ACCEPTABLE);
            }
            if (request.getRelatedtouserid() == null) {
                return new ResponseEntity("you need to defined the invoice relation " + request.getRelated(), HttpStatus.NOT_ACCEPTABLE);
            }
            relatedto = UserServices.findById(request.getRelatedtouserid());
            if (relatedto.getRole().getRole() == RoleEnum.ROLE_ADMIN || relatedto.getRole().getRole() == RoleEnum.ROLE_SUB_ADMIN || relatedto.getRole().getRole() == RoleEnum.ROLE_USER) {
                return new ResponseEntity("Admin or sub-admin or customer cannot be related", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        Set<Product> products = this.productservice.GetProductList(request.getProducts());
        Invoice invoice = new Invoice();
        if (request.getThirdpartypoid() != null) {
            if (this.service.existByThirdpartypoid(request.getThirdpartypoid()))
                return new ResponseEntity(request.getThirdpartypoid() + "is already found , please try something else !", HttpStatus.FOUND);

            invoice.setThirdpartypoid(request.getThirdpartypoid());
        }
        if (request.getInvoicenumber() != null) {
            invoice.setInvoicenumber(request.getInvoicenumber());
        }
        if (request.getInvoicedate() != null) {
            invoice.setInvoicedate(request.getInvoicedate());

        }
        if (request.getDuedate() != null) {
            invoice.setDuedate(request.getDuedate());

        }
        if (request.getCurrency() != null) {
            invoice.setCurrency(request.getCurrency());

        }
        if (request.getSuppliername() != null) {
            invoice.setSuppliername(request.getSuppliername());

        }
        if (request.getSupplierzipcode() != null) {
            invoice.setSupplierzipcode(request.getSupplierzipcode());

        }
        if (request.getSupplierstreetadress() != null) {
            invoice.setSupplierstreetadress(request.getSupplierstreetadress());

        }
        if (request.getSupplierphonenumber() != null) {
            invoice.setSupplierphonenumber(request.getSupplierphonenumber());

        }
        if (request.getBankname() != null) {
            invoice.setBankname(request.getBankname());

        }
        if (request.getBankzipcode() != null) {
            invoice.setBankzipcode(request.getBankzipcode());

        }
        if (request.getBankstreetadress() != null) {
            invoice.setBankstreetadress(request.getBankstreetadress());

        }
        if (request.getBankphonenumber() != null) {
            invoice.setBankphonenumber(request.getBankphonenumber());

        }
        if (request.getVehicleregistration() != null) {
            invoice.setVehicleregistration(request.getVehicleregistration());

        }
        if (request.getVehiclecolor() != null) {
            invoice.setVehiclecolor(request.getVehiclecolor());

        }
        if (request.getVehiclevin() != null) {
            invoice.setVehiclevin(request.getVehiclevin());

        }
        if (request.getVehiclemodel() != null) {
            invoice.setVehiclemodel(request.getVehiclemodel());

        }
        if (request.getVehiclemark() != null) {
            invoice.setVehiclemark(request.getVehiclemark());

        }
        if (request.getVehiclemileage() != null) {
            invoice.setVehiclemileage(request.getVehiclemileage());

        }
        if (request.getVehiclemotexpiry() != null) {
            invoice.setVehiclemotexpiry(request.getVehiclemotexpiry());

        }

        if (request.getVehicleenginesize() != null) {
            invoice.setVehicleenginesize(request.getVehicleenginesize());

        }
        if (products != null) {
            invoice.setProducts(products);

        }
        if (createdby != null) {
            invoice.setCreatedby(createdby);

        }
        if (request.getRelated() != null) {
            invoice.setRelated(request.getRelated());

        }
        if (relatedto != null) {
            invoice.setRelatedto(relatedto);

        }
        if (request.getBankiban() != null) {
            invoice.setBankiban(request.getBankiban());

        }
        if (request.getBankrip() != null) {
            invoice.setBankrib(request.getBankrip());

        }

        if (request.getVehicleprice() != null) {
            invoice.setVehicleprice(request.getVehicleprice());
        } else if (vehicles.getVehiclespricefinancing() != null) {
            invoice.setVehicleprice(vehicles.getVehiclespricefinancing().getTotalamount());
        }
        invoice.setPurshaseorder(po);
        Invoice result = this.service.Save(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @PatchMapping(value = {"/update_invoice_data/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Update invoice for admin and supplier", notes = "Endpoint to update an invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable, you need to define the invoice relation"),
            @ApiResponse(code = 302, message = "The invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> update_invoice_data(@RequestBody(required = false) InvoiceRequest request, @PathVariable Long invoice_id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        Invoice existingInvoice = this.service.FindById(invoice_id);
        if (existingInvoice == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_ACCEPTABLE);
        }
        if (existingInvoice.getStatus() == InvoiceStatus.CANCELLED) {
            return new ResponseEntity(invoice_id + " this invoice is canceled!", HttpStatus.NOT_ACCEPTABLE);
        }
        if (existingInvoice.getStatus() == InvoiceStatus.PAID) {
            return new ResponseEntity(invoice_id + " this invoice is paid!", HttpStatus.NOT_ACCEPTABLE);
        }
        if (existingInvoice.getStatus() == InvoiceStatus.REFUNDS) {
            return new ResponseEntity(invoice_id + " this invoice is refund!", HttpStatus.NOT_ACCEPTABLE);
        }

        // Update invoice number if provided
        if (request.getInvoicenumber() != null) {
            if (this.service.ExistByInvoiceNumber(request.getInvoicenumber())) {
                return new ResponseEntity(request.getInvoicenumber() + " is already found, please try something else!", HttpStatus.FOUND);
            }
            existingInvoice.setInvoicenumber(request.getInvoicenumber());
        }

        // Update invoice date if provided
        if (request.getInvoicedate() != null) {
            existingInvoice.setInvoicedate(request.getInvoicedate());
        }

        // Update other fields if provided
        if (request.getDuedate() != null) {
            existingInvoice.setDuedate(request.getDuedate());
        }
        if (request.getCurrency() != null) {
            existingInvoice.setCurrency(request.getCurrency());
        }
        if (request.getSuppliername() != null) {
            existingInvoice.setSuppliername(request.getSuppliername());
        }
        if (request.getSupplierzipcode() != null) {
            existingInvoice.setSupplierzipcode(request.getSupplierzipcode());
        }
        if (request.getSupplierstreetadress() != null) {
            existingInvoice.setSupplierstreetadress(request.getSupplierstreetadress());
        }
        if (request.getSupplierphonenumber() != null) {
            existingInvoice.setSupplierphonenumber(request.getSupplierphonenumber());
        }
        if (request.getBankname() != null) {
            existingInvoice.setBankname(request.getBankname());
        }
        if (request.getBankzipcode() != null) {
            existingInvoice.setBankzipcode(request.getBankzipcode());
        }
        if (request.getBankstreetadress() != null) {
            existingInvoice.setBankstreetadress(request.getBankstreetadress());
        }
        if (request.getBankphonenumber() != null) {
            existingInvoice.setBankphonenumber(request.getBankphonenumber());
        }
        if (request.getVehicleregistration() != null) {
            existingInvoice.setVehicleregistration(request.getVehicleregistration());
        }
        if (request.getVehiclecolor() != null) {
            existingInvoice.setVehiclecolor(request.getVehiclecolor());
        }
        if (request.getVehiclevin() != null) {
            Vehicles vehicles = this.vehiclesService.FindByVIN(request.getVehiclevin());
            if (vehicles == null)
                return new ResponseEntity(request.getVehiclevin() + "this vehicle VIN  not found", HttpStatus.NOT_ACCEPTABLE);
            existingInvoice.setVehiclevin(request.getVehiclevin());
        }
        if (request.getVehiclemodel() != null) {
            existingInvoice.setVehiclemodel(request.getVehiclemodel());
        }
        if (request.getVehiclemark() != null) {
            existingInvoice.setVehiclemark(request.getVehiclemark());
        }
        if (request.getVehiclemileage() != null) {
            existingInvoice.setVehiclemileage(request.getVehiclemileage());
        }
        if (request.getVehiclemotexpiry() != null) {
            existingInvoice.setVehiclemotexpiry(request.getVehiclemotexpiry());
        }
        if (request.getVehicleenginesize() != null) {
            existingInvoice.setVehicleenginesize(request.getVehicleenginesize());
        }
        if (request.getBankiban() != null) {
            existingInvoice.setBankiban(request.getBankiban());
        }
        if (request.getBankrip() != null) {
            existingInvoice.setBankrib(request.getBankrip());
        }
        if (request.getStatus() != null) {
            existingInvoice.setStatus(request.getStatus());
        }

        // Update products if provided
        if (request.getProducts() != null) {
            Set<Product> products = this.productservice.GetProductList(request.getProducts());
            existingInvoice.setProducts(products);
        }

        // Update related information if provided
        if (request.getRelated() != null) {
            existingInvoice.setRelated(request.getRelated());
        }

        // Save the updated invoice
        Invoice result = this.service.Update(existingInvoice);

        // Save new action
        UserAction action = new UserAction(UserActionsEnum.INVOICE_MANAGEMENT, user);
        this.userActionService.Save(action);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_invoice/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "update invoice for admin and supplier", notes = "Endpoint to update invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Invoice> update_invoice(@PathVariable Long invoice_id, @ModelAttribute String remark, @ModelAttribute InvoiceStatus status) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (this.service.FindById(invoice_id) == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Invoice invoice = this.service.FindById(invoice_id);
        if (remark != null) invoice.setRemark(remark);
        if (status != null) invoice.setStatus(status);
        Invoice result = this.service.Update(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @GetMapping(value = {"/admin/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get invoice for admin", notes = "Endpoint to get invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Invoice> admin_get_invoice_by_id(@PathVariable Long invoice_id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (this.service.FindById(invoice_id) == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Invoice result = this.service.FindById(invoice_id);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @GetMapping(value = {"/get_invoice_by_third_party_po_id/{thirdpartypoid}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER')  or hasRole('SUB_SUPPLIER') ")
    @ApiOperation(value = "get invoice for admin", notes = "Endpoint to get invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Invoice> get_invoice_by_third_party_po_id(@PathVariable String thirdpartypoid) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (this.service.FindByThirdpartypoid(thirdpartypoid) == null) {
            return new ResponseEntity(thirdpartypoid + " is not found in the system!", HttpStatus.NOT_ACCEPTABLE);
        }
        Invoice result = this.service.FindByThirdpartypoid(thirdpartypoid);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @GetMapping(value = {"/user_all_invoices/{user_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get user's invoices", notes = "Endpoint to get invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<DynamicResponse> all_user_invoices(@PathVariable Long user_id, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        users user = this.UserServices.findById(user_id);
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(this.service.getInvoicesByUser(page, size, user), HttpStatus.OK);


    }

    @GetMapping(value = {"/get_my_invoices"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or  hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get user's invoices", notes = "Endpoint to get invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<DynamicResponse> get_my_invoices(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(this.service.getInvoicesByUser(page, size, currentuser), HttpStatus.OK);

    }

    @GetMapping(value = {"/supplier/{invoice_id}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get invoice for supplier", notes = "Endpoint to get invoice for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, this request not related to this user"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the supplier"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Invoice> supplier_get_invoice_by_id(@PathVariable Long invoice_id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (this.service.FindById(invoice_id) == null) {
            return new ResponseEntity(invoice_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Invoice result = this.service.FindById(invoice_id);
        if (result.getCreatedby() != user && result.getRelatedto() != user)
            return new ResponseEntity(invoice_id + "you not related or the owner of this invoice", HttpStatus.BAD_REQUEST);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }

    @GetMapping(value = {"/invoice_report_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to invoices' reports for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<InvoiceReport> invoice_report_admin(@ModelAttribute InvoiceRepportRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        InvoiceReport report = new InvoiceReport();
        Date date = request.getDate();
        InvoiceRelated related = request.getRelated();
        if (date == null)
            date = new Date();
        System.out.println(date);
        report.setDate(date);
        report.setInvoicepermonth(this.service.countInvoicePerMonth(date, related));
        report.setRefundbymonth(this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.REFUNDS, related));
        report.setPaymentbymonth(this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.PAID, related) + this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.UNPAID, related));
        report.setPurshaseorderrequest(this.purshaseOrderService.countPurchaseOrdersWithCustomerOrSupllier(related));
        report.setRequestdone(this.requestService.countDoneRequestsByUserRole(related));
        report.setRequestpending(this.requestService.countRequestsByUserRoleAndStatus(related));
        report.setSoldcars(this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.PAID, related));
        report.setInvoicepermonth(this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.PAID, related) + this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.UNPAID, related) + this.service.countInvoicePerMonthAndStatus(date, InvoiceStatus.REFUNDS, related));
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(report, HttpStatus.OK);

    }


    @GetMapping(value = {"/invoice_report_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to invoices reportfor supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<InvoiceReport> invoice_report_supplier(@RequestParam(required = false) Date date) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Supplier supplier = user.getSupplier();
        if (supplier == null)
            return new ResponseEntity("this user is not supplier", HttpStatus.NOT_FOUND);
        InvoiceReport report = new InvoiceReport();

       /* Date date = request.getDate();
        InvoiceRelated related = request.getRelated();*/
        if (date == null)
            date = new Date();
        System.out.println(date);
        report.setDate(date);
        // report.setInvoicepermonth(this.service.countInvoicePerMonthAndUser(date, user));
        report.setRefundbymonth(this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.REFUNDS, user));
        report.setPaymentbymonth(this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.PAID, user) + this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.UNPAID, user));
        report.setPurshaseorderrequest(supplier.getPurchaseOrders().size());
        report.setRequestdone(this.requestService.countDoneRequestsByUser(user));
        report.setRequestpending(this.requestService.countPendingRequestsByUserAndStatus(user));
        report.setSoldcars(this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.PAID, user));
        report.setInvoicepermonth(this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.PAID, user) + this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.UNPAID, user) + this.service.countInvoicePerMonthAndStatusAndUser(date, InvoiceStatus.REFUNDS, user));
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(report, HttpStatus.OK);

    }


    @PatchMapping(value = {"/add_refund_invoice/{idInvoice}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add refund invoice for admin", notes = "Endpoint to add refund invoice for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> add_refund_invoice(@PathVariable Long idInvoice, @ModelAttribute RefundInvoiceRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Invoice invoice = this.service.FindById(idInvoice);
        if (invoice == null)
            return new ResponseEntity("no invoice founded with that id", HttpStatus.NOT_FOUND);

        if (invoice.getStatus() != InvoiceStatus.PAID)
            return new ResponseEntity("The invoice is not paid yet", HttpStatus.NOT_ACCEPTABLE);
        if (this.service.getTotalRevenueFromOnePaidInvoice(invoice) - request.getAmount() < 0)
            return new ResponseEntity("refunded amount is bigger then invoice amount ", HttpStatus.NOT_ACCEPTABLE);

        RefundInvoice refund = new RefundInvoice();
        refund.setAmount(request.getAmount());
        refund.setReason(request.getReason());
        refund.setInvoice(invoice);
        this.refundInvoiceService.Save(refund);

        invoice.setStatus(InvoiceStatus.REFUNDS);
        Invoice result = this.service.Update(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PatchMapping(value = {"/confirm_invoice/{idInvoice}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "confirm invoice for admin", notes = "Endpoint to confirm invoice for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> confirm_invoice(@PathVariable Long idInvoice) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_ACCEPTABLE);
        Invoice invoice = this.service.FindById(idInvoice);

        if (invoice == null)
            return new ResponseEntity("no invoice founded with that id", HttpStatus.NOT_ACCEPTABLE);

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.REJECTED)
            return new ResponseEntity("The invoice is already paid or rejected", HttpStatus.NOT_ACCEPTABLE);
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setConfirmedBy(user);
        Invoice result = this.service.Update(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PatchMapping(value = {"/reject_invoice/{idInvoice}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "confirm invoice for admin", notes = "Endpoint to reject invoice for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> reject_invoice(@PathVariable Long idInvoice) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_ACCEPTABLE);
        Invoice invoice = this.service.FindById(idInvoice);
        if (invoice == null)
            return new ResponseEntity("no invoice founded with that id", HttpStatus.NOT_ACCEPTABLE);

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.REJECTED || invoice.getStatus() == InvoiceStatus.REFUNDS)
            return new ResponseEntity("The invoice is already paid ", HttpStatus.NOT_ACCEPTABLE);
        if (invoice.getConfirmedBy() != null && !invoice.getConfirmedBy().equals(user))
            return new ResponseEntity("The invoice is already confirmed by " + invoice.getConfirmedBy().getPersonalinformation().getFirstnameen() + " " + invoice.getConfirmedBy().getPersonalinformation().getLastnameen(), HttpStatus.NOT_ACCEPTABLE);
        invoice.setStatus(InvoiceStatus.REJECTED);
        invoice.setConfirmedBy(user);
        Invoice result = this.service.Update(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PatchMapping(value = {"/paid_invoice/{idInvoice}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "confirm invoice for admin", notes = "Endpoint to change invoice's status to pia for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> payed_invoice(@PathVariable Long idInvoice) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_ACCEPTABLE);
        Invoice invoice = this.service.FindById(idInvoice);
        if (invoice == null)
            return new ResponseEntity("no invoice founded with that id", HttpStatus.NOT_ACCEPTABLE);

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.REJECTED)
            return new ResponseEntity("The invoice is already paid or rejected", HttpStatus.NOT_ACCEPTABLE);
        if (invoice.getConfirmedBy() != null && !invoice.getConfirmedBy().equals(user))
            return new ResponseEntity("The invoice is already confirmed by " + invoice.getConfirmedBy().getPersonalinformation().getFirstnameen() + " " + invoice.getConfirmedBy().getPersonalinformation().getLastnameen(), HttpStatus.NOT_ACCEPTABLE);

        //update invoice status and confirmed by status
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setConfirmedBy(user);
        Invoice result = this.service.Update(invoice);

        //update vehicle availibility
        Vehicles vehicles = this.vehiclesService.FindByVIN(invoice.getVehiclevin());
        if (vehicles == null)
            return new ResponseEntity("no vehicle founded with that carvin", HttpStatus.NOT_ACCEPTABLE);
        vehicles.setAvailiability(AvailiabilityEnum.SOLD);
        this.vehiclesService.Update(vehicles);

        //update purchase order status
        PurshaseOrder po = invoice.getPurshaseorder();
        if (po == null)
            return new ResponseEntity("no po founded to update po status", HttpStatus.NOT_ACCEPTABLE);
        po.setStatus(PurshaseOrderStatus.COMPLETED);
        this.purshaseOrderService.Update(po);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
