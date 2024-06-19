package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.SupplierClassRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.SupplierClassificationService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/suppliers_classification")
public class SupplierClassificationController extends BaseController {

    @Autowired
    private SupplierClassificationService service;

    @Autowired
    private UserService userService;

    @Autowired
    private UserActionService userActionService;


    @GetMapping(value = {"/all_suppliers_classifications_list_by_name"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all suppliers classification for admin by name", notes = "Endpoint to get suppliers classification list by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<SuppliersClassification>> all_categories_by_name(@RequestParam(required = false) String name) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        List<SuppliersClassification> result = new ArrayList<>();
        if (name == null) {
            result = this.service.FindAll();
        } else {
            result = this.service.FindAllByNameList(name);
        }

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_suppliers_classifications_pg_by_name"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all suppliers classification for admin by name", notes = "Endpoint to get suppliers classification paginator by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_categories_paginator(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String name) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        DynamicResponse  result = new DynamicResponse();
        if (name == null) {
            result = this.service.FindAllPg(page, size);
        } else {
            result = this.service.FindAllByNamePg(page, size, name);
        }

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/classification_by_id/{classification_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "delete category by id for admin ", notes = "Endpoint to get classification by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<SuppliersClassification> classification_by_id(@PathVariable Long classification_id) throws IOException {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentUser == null)
            return new ResponseEntity("Current user not found", HttpStatus.NOT_FOUND);

        SuppliersClassification classification = this.service.FindById(classification_id);
        if (classification == null)
            return new ResponseEntity("classification not found with id: " + classification_id, HttpStatus.NOT_FOUND);
        this.service.Update(classification);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                currentUser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(classification, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_classification"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new suppliers classification from the admin", notes = "Endpoint to add a new new suppliers classification for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the category"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<SuppliersClassification> add_category(@ModelAttribute SupplierClassRequest request) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

        if (request.getName() == null)
            return new ResponseEntity("Name can't be null or empty", HttpStatus.BAD_REQUEST);
        SuppliersClassification classification = new SuppliersClassification();
        classification.setName(request.getName());

        if (request.getDescription() == null)
            classification.setDescription(request.getDescription());

        if(request.getUsersId() != null && !request.getUsersId().isEmpty()){
            for(Long id : request.getUsersId()){
                users u = this.userService.findById(id);
                if(u == null)
                    return new ResponseEntity("User not found with this id :" + id, HttpStatus.NOT_FOUND);
                if(u.getSupplier() == null)
                    return new ResponseEntity("this id: "+ id +  " does not belong to supplier:" + id, HttpStatus.NOT_FOUND);
                classification.getSuppliers().add(u);
            }
        }
        SuppliersClassification result = this.service.Save(classification);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PatchMapping("/update_classification/{classification_id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Update an existing category", notes = "Endpoint to update an existing classification for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the category"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Category not found")
    })
    public ResponseEntity<SuppliersClassification> update_category(@PathVariable Long classification_id, @ModelAttribute SupplierClassRequest request) {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentUser == null)
            return new ResponseEntity("Current user not found", HttpStatus.NOT_FOUND);

        SuppliersClassification classification = this.service.FindById(classification_id);
        if (classification == null)
            return new ResponseEntity("classification not found with id: " + classification_id, HttpStatus.NOT_FOUND);

        if (request.getName() != null)
            classification.setName(request.getName());

        if (request.getDescription() != null)
            classification.setDescription(request.getDescription());

        if (request.getUsersId() != null && !request.getUsersId().isEmpty()){
            classification.getSuppliers().clear();
            for(Long id : request.getUsersId()){
                users u = this.userService.findById(id);
                if(u == null)
                    return new ResponseEntity("User not found with this id :" + id, HttpStatus.NOT_FOUND);
                if(u.getSupplier() == null)
                    return new ResponseEntity("this id: "+ id +  " does not belong to supplier:" + id, HttpStatus.NOT_FOUND);
                classification.getSuppliers().add(u);
            }

        }

        SuppliersClassification result = this.service.Update(classification);

        // Save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                currentUser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/delete_classification/{classification_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "delete category by id for admin ", notes = "Endpoint to delete classification by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity delete_category(@PathVariable Long classification_id) throws IOException {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentUser == null)
            return new ResponseEntity("Current user not found", HttpStatus.NOT_FOUND);

        SuppliersClassification classification = this.service.FindById(classification_id);
        if (classification == null)
            return new ResponseEntity("classification not found with id: " + classification_id, HttpStatus.NOT_FOUND);
        classification.setArchive(true);
        this.service.Update(classification);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                currentUser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}