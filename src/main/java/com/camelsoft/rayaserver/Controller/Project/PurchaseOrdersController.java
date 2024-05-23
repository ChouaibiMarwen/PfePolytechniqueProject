package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.DTO.PurchaseOrderDto;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.File.FilesStorageService;
import com.camelsoft.rayaserver.Request.project.PurshaseOrderRequest;
import com.camelsoft.rayaserver.Request.project.VehiclesRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.PurshaseOrderService;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/purchase_orders")
public class PurchaseOrdersController  extends BaseController {

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private PurshaseOrderService purshaseOrderService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierServices supplierServices;


    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "add purshase order for supplier", notes = "Endpoint to add purshase order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<PurchaseOrderDto> add_vehicle(@ModelAttribute PurshaseOrderRequest request) throws IOException{
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        Vehicles vehicle =  this.vehiclesService.FindById(request.getVehicleId());
        if(!this.vehiclesService.inStock(vehicle, request.getQuantity()))
            return new ResponseEntity("No disponible quantity for this vehicle id selected", HttpStatus.BAD_REQUEST);

        users user = this.userService.findById(request.getSupplierId());
        if(user == null ){
            return new ResponseEntity("User is not founded", HttpStatus.BAD_REQUEST);
        }
        Supplier supplier = user.getSupplier();
        Vehicles vehicles = this.vehiclesService.FindById(request.getVehicleId());
        if(vehicles == null ){
            return new ResponseEntity("vehicle is not founded", HttpStatus.BAD_REQUEST);
        }
        PurshaseOrder purshaseOrder = new PurshaseOrder();
        purshaseOrder.setVehicleId(request.getVehicleId());
        purshaseOrder.setSupplierId(supplier.getId());
        purshaseOrder.setOrderDate(request.getOrderDate());
        purshaseOrder.setStatus(request.getStatus());
        purshaseOrder.setSupplier(supplier);
        purshaseOrder.setVehicles(vehicles);
        if( vehicles.getStock() - request.getQuantity() < 0)
            return new ResponseEntity(" vehiclas Quantity demanded is not founded in stock", HttpStatus.NOT_ACCEPTABLE);
        purshaseOrder.setQuantity(request.getQuantity());
        purshaseOrder.setDiscountamount(request.getDiscountamount());
        purshaseOrder.setRequestDeliveryDate(request.getRequestDeliveryDate());
        purshaseOrder.setStreetAddress(request.getStreetAddress());
        purshaseOrder.setCity(request.getCity());
        purshaseOrder.setState(request.getState());
        purshaseOrder.setCodePostal(request.getCodePostal());
        purshaseOrder.setCountry(request.getCountry());
        purshaseOrder.setDescription(request.getDescription());
        if(!request.getAttachments().isEmpty()){
            Set<File_model> attachmentsList = new HashSet<>();
            if (this.filesStorageService.checkformatList(request.getAttachments())) {
                attachmentsList = filesStorageService.save_all(request.getAttachments(), "purshase_order");
                if (attachmentsList == null || attachmentsList.isEmpty()) {
                    return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
                }
            }
            purshaseOrder.setAttachments(attachmentsList);
        }
        PurshaseOrder po = this.purshaseOrderService.Save(purshaseOrder);
        PurchaseOrderDto purchaseOrderDto = PurchaseOrderDto.PurchaseOrderToDto(po);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);

        return  new ResponseEntity<>(purchaseOrderDto, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_purchase_orders_by_status"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by status for admin by name", notes = "Endpoint to get purchase orders by status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_purchase_oreder_by_status(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size , @RequestParam(required = false) String status) throws IOException {
        DynamicResponse result = this.purshaseOrderService.findAllPgByStatus(page, size , status);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_purchase_orders_by_status_and_date"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by status and date for admin by name", notes = "Endpoint to get purchase orders by status and date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_purchase_oreder_byStatus_And_date(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size , @RequestParam(required = false) PurshaseOrderStatus status, @RequestParam(required = false) Date creationdate) throws IOException {
        DynamicResponse result = this.purshaseOrderService.findAllPgByStatusAndDate(page, size , status , creationdate);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_purchase_orders_by_status_and_date_and_vehicle"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by status and date and vehicle for admin by name", notes = "Endpoint to get purchase orders by status and date and vehicle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_purchase_oreder_byStatus_And_date_and_vehicle(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size , @RequestParam(required = false) PurshaseOrderStatus status, @RequestParam(required = false) Date creationdate ,  @RequestParam(required = false) Long idVehicles) throws IOException {
        if(idVehicles != null){
            Vehicles vehicles = this.vehiclesService.FindById(idVehicles);
            if (vehicles == null)
                return new ResponseEntity("Vehecle is not founded by that id", HttpStatus.NOT_FOUND);
            DynamicResponse result = this.purshaseOrderService.FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatus(page, size , vehicles ,status , creationdate);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        DynamicResponse result = this.purshaseOrderService.findAllPgByStatusAndDate(page, size ,status , creationdate);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_purchase_orders_by_status_and_date_and_vehicle_and_supplier"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by status and date and vehicle and supplier  for admin by name", notes = "Endpoint to get purchase orders by status and date and vehicle and supplier for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> allPurchaseOrdersByStatusAndDateAndVehicleAndSupplier(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size , @RequestParam(required = false) PurshaseOrderStatus status, @RequestParam(required = false) Date creationdate ,  @RequestParam(required = false) Long idvehecle,  @RequestParam(required = false) Long idSupplier) throws IOException {

        DynamicResponse result = this.purshaseOrderService.FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatusAndSupplier(page, size ,idvehecle ,status , creationdate, idSupplier);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @PatchMapping(value ="/update_purchase_order/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all purchase orders by status for admin by name", notes = "Endpoint to get purchase orders by status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> all_purchase_oreder_by_status(@PathVariable Long purchaseOrderId, @ModelAttribute PurshaseOrderRequest request ) throws IOException{
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        if (request.getOrderDate() != null) {
            purchaseOrder.setOrderDate(request.getOrderDate());
        }
        if (request.getStatus() != null) {
            purchaseOrder.setStatus(request.getStatus());
        }
        if (request.getSupplierId() != null) {
            purchaseOrder.setSupplierId(request.getSupplierId());
            users user = this.userService.findById(request.getSupplierId());
            if(user == null ){
                return new ResponseEntity("User is not founded", HttpStatus.BAD_REQUEST);
            }
            Supplier supplier = user.getSupplier();
            purchaseOrder.setSupplier(supplier);
        }
        if (request.getVehicleId() != null) {
            purchaseOrder.setVehicleId(request.getVehicleId());
            Vehicles vehicles = this.vehiclesService.FindById(request.getVehicleId());
            if(vehicles == null ){
                return new ResponseEntity("vehicle is not founded", HttpStatus.BAD_REQUEST);
            }
            purchaseOrder.setVehicles(vehicles);
        }
        if (request.getQuantity() != null) {
            purchaseOrder.setQuantity(request.getQuantity());
        }
        if (request.getDiscountamount() != null) {
            purchaseOrder.setDiscountamount(request.getDiscountamount());
        }
        if (request.getRequestDeliveryDate() != null) {
            purchaseOrder.setRequestDeliveryDate(request.getRequestDeliveryDate());
        }
        if (request.getCity() != null) {
            purchaseOrder.setCity(request.getCity());
        }
        if (request.getState() != null) {
            purchaseOrder.setState(request.getState());
        }
        if (request.getCodePostal() != null) {
            purchaseOrder.setCodePostal(request.getCodePostal());
        }
        if (request.getCountry() != null) {
            purchaseOrder.setCountry(request.getCountry());
        }
        if (request.getDescription() != null) {
            purchaseOrder.setDescription(request.getDescription());
        }

        PurshaseOrder po = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto purchaseOrderDto = PurchaseOrderDto.PurchaseOrderToDto(po);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(purchaseOrderDto, HttpStatus.OK);

    }


    @GetMapping(value = {"/purchase_order/{id]"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by id for admin ", notes = "Endpoint to get purchase orders by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> getPurchaseOrderById(@PathVariable(required = false) Long id) throws IOException {
        PurshaseOrder result = this.purshaseOrderService.FindById(id);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(result);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }


    @PatchMapping(value = "/send_purchase_order/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "accept and send purchase order by admin", notes = "Endpoint send purchase order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> acceptAndSendPurchaseOrderr(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(PurshaseOrderStatus.PENDING);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }


    @PatchMapping(value = "/update_purchase_order_status/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "update purchase order status by admin", notes = "Endpoint update purchase order status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> updatePurchaseOrderStatus(@PathVariable Long purchaseOrderId, @RequestParam PurshaseOrderStatus status) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(status);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

    @PatchMapping(value = "/accept_purchase_order_by_Supplier/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "accept purchase order status by supplier", notes = "Endpoint to accept purchase order status by supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> acceptPurchaseOrderBySupplier(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(PurshaseOrderStatus.ACCEPTED);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

    @PatchMapping(value = "/reject_purchase_order/{purchaseOrderId}")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "reject purchase order status by supplier", notes = "Endpoint to reject purchase order status by supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> rejectPurchaseOrderBySupplier(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(PurshaseOrderStatus.REJECTED);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }


    @PatchMapping(value = "/accept_purchase_order_by_Admin/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "accept purchase order status by admin", notes = "Endpoint to accept purchase order status by admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> acceptPurchaseOrderByAdmin(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(PurshaseOrderStatus.IN_PROGRESS);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }



    @PatchMapping(value = "/cancel_purchase_order/{purchaseOrderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "cancel purchase order status by admin", notes = "Endpoint to cancel purchase order status by admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurchaseOrderDto> cancelPurchaseOrderByAdmin(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder purchaseOrder =  this.purshaseOrderService.FindById(purchaseOrderId);
        if(purchaseOrder == null)
            return new ResponseEntity("purchase order is not founded ", HttpStatus.NOT_FOUND);
        purchaseOrder.setStatus(PurshaseOrderStatus.CANCELED);
        PurshaseOrder purshaseOrder1 = this.purshaseOrderService.Update(purchaseOrder);
        PurchaseOrderDto po = PurchaseOrderDto.PurchaseOrderToDto(purshaseOrder1);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("can't get the current user", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PURCHASE_ORDER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(po, HttpStatus.OK);
    }


}
