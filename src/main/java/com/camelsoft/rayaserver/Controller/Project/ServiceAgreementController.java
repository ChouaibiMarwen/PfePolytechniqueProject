package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.Service_Agreement;

import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.ServiceAgreementRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.ServiceAgreementService;
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


import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/service_agreement")
public class ServiceAgreementController extends BaseController {
    @Autowired
    private UserActionService userActionService;

    @Autowired
    private ServiceAgreementService service;

    @Autowired
    private UserService userService;

    @Autowired
    private SupplierServices supplierServices;


    @PostMapping(value = {"/add_service_agreement/{supplierId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "Add added service agreements to a supplier from the admin", notes = "Endpoint to add service agreements list to supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added service agreements to supplier"),
            @ApiResponse(code = 400, message = "Bad request, empty or null title or empty and null content in request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Service_Agreement> addServiceAgreements(@PathVariable Long supplierId, @ModelAttribute ServiceAgreementRequest request) throws IOException {
        if(request.getTitle() == null || request.getTitle().isEmpty())
            return new ResponseEntity("Title can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getContant() == null)
            return new ResponseEntity("content can't be null or empty", HttpStatus.BAD_REQUEST);
        users user = this.userService.findById(supplierId);
        if(user == null)
            return new ResponseEntity("no user founded using this id: " + supplierId, HttpStatus.NOT_FOUND);
        Supplier supplier = user.getSupplier();
        if(supplier == null)
            return new ResponseEntity("no supplier founded using this id: " + supplierId, HttpStatus.NOT_FOUND);

        try{
            Service_Agreement agreement = new Service_Agreement();
            agreement.setTitle(request.getTitle());
            agreement.setContent(request.getContant());
            agreement.setSupplier(supplier);
            Service_Agreement result =  this.service.Save(agreement);
            users currentuser = userService.findByUserName(getCurrentUser().getUsername());
            //save new action
            UserAction action = new UserAction(
                    UserActionsEnum.SUPPLIER_MANAGEMENT,
                    currentuser
            );
            this.userActionService.Save(action);
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
    public ResponseEntity<Service_Agreement> daleteServiceAgreement(@PathVariable Long serviceAgreementId){
        Service_Agreement serviceAgreement =  this.service.FindById(serviceAgreementId);
        if(serviceAgreement == null)
            return new ResponseEntity("Service agreement by this id :" + serviceAgreementId + "is not founded ", HttpStatus.NOT_FOUND);
        Supplier supplier = serviceAgreement.getSupplier();
        supplier.getServiceagreements().remove(serviceAgreement);
        this.supplierServices.update(supplier);
        serviceAgreement.setDeleted(true);
        this.service.Update(serviceAgreement);
            serviceAgreement = this.service.Update(serviceAgreement);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity(serviceAgreement,HttpStatus.OK);
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
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
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
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(serviceAgreement, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_service_arguments_by_supplier/{supplierId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all Service agreements list by supplier paginated for admin", notes = "Endpoint toget all Service agreements list by supplier paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin or supplier")
    })
    public ResponseEntity<List<Service_Agreement>> all_invoice_by_purchase_order(@PathVariable Long supplierId) throws IOException {
        users user = this.userService.findById(supplierId);
        if(user == null)
            return new ResponseEntity("no user founded using this id: " + supplierId, HttpStatus.NOT_FOUND);
        Supplier supplier = user.getSupplier();
        if(supplier == null)
            return new ResponseEntity("no supplier founded using this id: " + supplierId, HttpStatus.NOT_FOUND);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(this.service.findAllBySupplier(supplier), HttpStatus.OK);

    }


    @GetMapping(value = {"/all_service_arguments_by_supplier_paginated/{supplierId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    @ApiOperation(value = "get all Service agreements by supplier paginated for admin", notes = "Endpoint toget all Service agreements by supplier paginated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin or supplier")
    })
    public ResponseEntity<DynamicResponse> all_invoice_by_purchase_order_paginated(@PathVariable Long supplierId, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = this.userService.findById(supplierId);
        if(user == null)
            return new ResponseEntity("no user founded using this id: " + supplierId, HttpStatus.NOT_FOUND);
        Supplier supplier = user.getSupplier();
        if(supplier == null)
            return new ResponseEntity("no supplier founded using this id: " + supplierId, HttpStatus.NOT_FOUND);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(this.service.findBySupplierPg(page, size, supplier), HttpStatus.OK);

    }



}
