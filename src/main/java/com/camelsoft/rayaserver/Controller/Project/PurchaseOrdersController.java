package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Repository.File.FilesStorageService;
import com.camelsoft.rayaserver.Request.project.PurshaseOrderRequest;
import com.camelsoft.rayaserver.Request.project.VehiclesRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.PurshaseOrderService;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/purchase_orders")
public class PurchaseOrdersController {

    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private PurshaseOrderService purshaseOrderService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;


    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "add purshase order for supplier", notes = "Endpoint to add purshase order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<PurshaseOrder> add_vehicle(@ModelAttribute PurshaseOrderRequest request) throws IOException{
        Vehicles vehicle =  this.vehiclesService.FindById(request.getVehicleId());
        if(!this.vehiclesService.inStock(vehicle, request.getQuantity()))
            return new ResponseEntity("No disponible quantity for this vehicle id selected", HttpStatus.BAD_REQUEST);

        PurshaseOrder po = new PurshaseOrder(
                request.getOrderDate(),
                request.getStatus(),
                request.getSupplierId(),
                request.getQuantity(),
                request.getVehicleId(),
                request.getDiscountamount(),
                request.getRequestDeliveryDate(),
                request.getStreetAddress(),
                request.getCity(),
                request.getCodePostal(),
                request.getState(),
                request.getCountry(),
                request.getDescription()
        );

        if(!(request.getAttachments() == null && request.getAttachments().isEmpty())){
            Set<File_model> attachmentsList = new HashSet<>();
            if (this.filesStorageService.checkformatList(request.getAttachments())) {
                attachmentsList = filesStorageService.save_all(request.getAttachments(), "purshase_order");
                if (attachmentsList == null || attachmentsList.isEmpty()) {
                    return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
                }
            }
            po.setAttachments(attachmentsList);
        }
        PurshaseOrder purshaseOrder = this.purshaseOrderService.Save(po);


        return  new ResponseEntity<>(purshaseOrder, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_purchase_orders_by_status"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all purchase orders by status for admin by name", notes = "Endpoint to get purchase orders by status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_purchase_oreder_by_status(int page, int size , @RequestParam(required = false) String status) throws IOException {
        DynamicResponse result = this.purshaseOrderService.findAllPgByStatus(page, size , status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value ="/update_purchase_order/{purchaseOrderId}")
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "get all purchase orders by status for admin by name", notes = "Endpoint to get purchase orders by status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurshaseOrder> all_purchase_oreder_by_status(@PathVariable Long purchaseOrderId, @ModelAttribute PurshaseOrderRequest request ) throws IOException{
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
        }
        if (request.getVehicleId() != null) {
            purchaseOrder.setVehicleId(request.getVehicleId());
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

        this.purshaseOrderService.Update(purchaseOrder);
        return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);

    }


    @GetMapping(value = {"/purchase_order/{id]"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all purchase orders by id for admin by name", notes = "Endpoint to get purchase orders by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<PurshaseOrder> getPurchaseOrderById(@PathVariable(required = false) Long id) throws IOException {
        PurshaseOrder result = this.purshaseOrderService.FindById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
