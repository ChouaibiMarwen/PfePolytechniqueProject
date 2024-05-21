package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.DTO.PurchaseOrderDto;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Service_Agreement;
import com.camelsoft.rayaserver.Repository.Project.ServiceAgreementRepository;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
import com.camelsoft.rayaserver.Request.project.ServiceAgreementRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.PurshaseOrderService;
import com.camelsoft.rayaserver.Services.Project.ServiceAgreementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/requests")
public class ServiceAgreementController {
    @Autowired
    private ServiceAgreementService service;

    @Autowired
    private PurshaseOrderService purchaseorderservice;


    @PostMapping(value = {"/add_service_agreement/{purchaseorderId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "Add added service agreements to purchase order from the admin", notes = "Endpoint to add service agreements list to purchase order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added service agreements to purchase order"),
            @ApiResponse(code = 400, message = "Bad request, empty or null title or empty and null content in request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Service_Agreement> addServiceAgreements(@PathVariable Long purchaseorderId, @ModelAttribute ServiceAgreementRequest request) throws IOException {
        if(request.getTitle() == null || request.getTitle().isEmpty())
            return new ResponseEntity("Title can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getContant() == null)
            return new ResponseEntity("content can't be null or empty", HttpStatus.BAD_REQUEST);
        PurshaseOrder  po = this.purchaseorderservice.FindById(purchaseorderId);
        if(po == null)
            return new ResponseEntity("no purchase orded founded using this id: " + purchaseorderId, HttpStatus.BAD_REQUEST);
        try{
            Service_Agreement agreement = new Service_Agreement();
            agreement.setTitle(request.getTitle());
            agreement.setContent(request.getContant());
            agreement.setPurchaseorder(po);
            Service_Agreement result =  this.service.Save(agreement);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity("Error adding service agreements", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PatchMapping(value = {"delete/{serviceAgreementId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "delete service agreement from the admin and the supplier", notes = "Endpoint to delete service agreement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully delete service agreements"),
            @ApiResponse(code = 400, message = "Bad request, empty or null title or empty and null content in request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity daleteServiceAgreement(@PathVariable Long serviceAgreementId){
        Service_Agreement serviceAgreement =  this.service.FindById(642828L);
        if(serviceAgreement == null)
            return new ResponseEntity("Service agreement by this id :" + serviceAgreementId + "is not founded ", HttpStatus.NOT_FOUND);
        PurshaseOrder purchaseOrder = serviceAgreement.getPurchaseorder();
        serviceAgreement.getPurchaseorder().getServiceagreements().remove(serviceAgreement);
        serviceAgreement.setDeleted(true);
        this.service.Update(serviceAgreement);
        if (purchaseOrder != null) {
            Set<Service_Agreement> serviceAgreements = purchaseOrder.getServiceagreements();
            serviceAgreements.remove(serviceAgreement);
            purchaseOrder.setServiceagreements(serviceAgreements);
            this.purchaseorderservice.Update(purchaseOrder); // Update the PurchaseOrder

        }
            serviceAgreement = this.service.Update(serviceAgreement);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PatchMapping(value = {"update_service_agreement/{serviceAgreementId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "update service agreement from the admin and the supplier", notes = "Endpoint to update service agreement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update service agreements"),
            @ApiResponse(code = 400, message = "Bad request,check title and content types in request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Service_Agreement> update_service_agreement(@PathVariable Long serviceAgreementId, @RequestParam(required = false) String title, @RequestParam(required = false) String content){
        Service_Agreement serviceAgreement =  this.service.FindById(serviceAgreementId);
        if(serviceAgreement == null || serviceAgreement.getDeleted())
            return new ResponseEntity("Service agreement by this id :" + serviceAgreementId + "is not founded ", HttpStatus.NOT_FOUND);

        if(title != null)
            serviceAgreement.setTitle(title);
        if(content != null)
            serviceAgreement.setContent(content);
        serviceAgreement = this.service.Update(serviceAgreement);
        return new ResponseEntity<>(serviceAgreement, HttpStatus.OK);
    }

    @GetMapping(value = {"/service_agreement/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get service agreement by id for admin and supplier ", notes = "Endpoint to get event by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Service_Agreement> getserviceAgreementById(@PathVariable Long id) throws IOException {
        Service_Agreement serviceAgreement =  this.service.FindById(id);
        if(serviceAgreement == null || serviceAgreement.getDeleted())
            return new ResponseEntity("Service agreement by this id :" + id + "is not founded ", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(serviceAgreement, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_service_arguments_by_purchase_order/{purchaseOrderId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all Service agreements by purchase order paginated for admin", notes = "Endpoint toget all Service agreements by purchase order paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin or supplier")
    })
    public ResponseEntity<List<Service_Agreement>> all_invoice_by_purchase_order(@PathVariable Long purchaseOrderId) throws IOException {
        PurshaseOrder  po = this.purchaseorderservice.FindById(purchaseOrderId);
        if(po == null)
            return new ResponseEntity("no purchase orded founded using this id: " + purchaseOrderId, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(this.service.findAllByPurchaseorder(po), HttpStatus.OK);

    }


    @GetMapping(value = {"/all_service_arguments_by_purchase_order_paginated/{purchaseOrderId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all Service agreements by purchase order paginated for admin", notes = "Endpoint toget all Service agreements by purchase order paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin or supplier")
    })
    public ResponseEntity<DynamicResponse> all_invoice_by_purchase_order_paginated(@PathVariable Long purchaseOrderId, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        PurshaseOrder  po = this.purchaseorderservice.FindById(purchaseOrderId);
        if(po == null)
            return new ResponseEntity("no purchase orded founded using this id: " + purchaseOrderId, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(this.service.findByPurchaseorderPg(page, size, po), HttpStatus.OK);

    }



}
