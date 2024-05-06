package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Repository.File.FilesStorageService;
import com.camelsoft.rayaserver.Request.project.PurshaseOrderRequest;
import com.camelsoft.rayaserver.Request.project.VehiclesRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
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
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/purchase_orders")
public class PurchaseOrdersController {

    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

/*
    @PostMapping(value = "/add_purshase_order")
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
        *//*this.*//*


        return  new ResponseEntity<>(po, HttpStatus.OK);
    }*/
}
