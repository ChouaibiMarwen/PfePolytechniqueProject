package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.*;
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

    @GetMapping(value = {"/all_invoice"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) InvoiceStatus status, @RequestParam(required = true) InvoiceRelated related) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);

        if (related == InvoiceRelated.NONE)
            return new ResponseEntity("you need to choose related NONE not a related", HttpStatus.NOT_ACCEPTABLE);
        if (status != null)
            return new ResponseEntity<>(this.service.FindAllByState(page, size, status, related), HttpStatus.OK);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        // return new ResponseEntity<>(this.service.FindAllPg(page, size, related), HttpStatus.OK);
        return new ResponseEntity<>(this.service.FindAllPg(page, size, related), HttpStatus.OK);


    }

    @GetMapping(value = {"/all_invoice_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all invoice by status for supplier", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_invoice_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) InvoiceStatus status, @RequestParam(required = false) InvoiceRelated related) throws IOException {

        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        Page<Invoice> invoice = this.criteriaService.findAllByStatusAndRelatedAndUsers(page, size, status, related, user);
        DynamicResponse res= new DynamicResponse(invoice.getContent(), invoice.getNumber(), invoice.getTotalElements(), invoice.getTotalPages());

        // return new ResponseEntity<>(this.service.FindAllPg(page, size, related), HttpStatus.OK);
        return new ResponseEntity<>(res, HttpStatus.OK);


    }

    @PostMapping(value = {"/add_invoice/{poid}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "add invoice for admin  and supplier", notes = "Endpoint to add invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 406, message = "Not acceptable , you need to defined the invoice relation"),
            @ApiResponse(code = 302, message = "the invoice number is already in use"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Invoice> add_invoice(@RequestBody InvoiceRequest request, @PathVariable Long poid) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        PurshaseOrder po = purshaseOrderService.FindById(poid);
        if (po == null)
            return new ResponseEntity("this po not found", HttpStatus.NOT_FOUND);
        if (po.getInvoice() != null)
            return new ResponseEntity("this po already have invoice", HttpStatus.NOT_ACCEPTABLE);
        Vehicles vehicles = this.vehiclesService.FindByVIN(request.getVehiclevin());
        if (vehicles == null)
            return new ResponseEntity(request.getVehiclevin() + "this vehicle VIN  not found", HttpStatus.NOT_ACCEPTABLE);
        users createdby = UserServices.findByUserName(getCurrentUser().getUsername());
        users relatedto = UserServices.findById(request.getRelatedtouserid());

        if (this.service.ExistByInvoiceNumber(request.getInvoicenumber())) {
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
                createdby,
                request.getRelated(),
                relatedto,
                request.getBankiban(),
                request.getBankrip()
        );
        invoice.setVehicleprice(vehicles.getPrice());
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

    @PatchMapping(value = {"/update_invoice/{invoice_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
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
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
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
    @ApiOperation(value = "get all invoice by status for admin", notes = "Endpoint to get vehicles")
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

    @PatchMapping(value = {"/add_refund_invoice/{idInvoice}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
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
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Invoice invoice = this.service.FindById(idInvoice);
        if (invoice == null)
            return new ResponseEntity("no invoice founded with that id", HttpStatus.NOT_FOUND);

        if (invoice.getStatus() == InvoiceStatus.PAID)
            return new ResponseEntity("The invoice is already paid", HttpStatus.NOT_ACCEPTABLE);

        invoice.setStatus(InvoiceStatus.PAID);
        Invoice result = this.service.Update(invoice);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.INVOICE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}
