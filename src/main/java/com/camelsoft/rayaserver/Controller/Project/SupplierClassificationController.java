package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.UsersCategoryRequest;
import com.camelsoft.rayaserver.Services.Project.SupplierClassificationService;
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
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/suppliers_classification")
public class SupplierClassificationController extends BaseController {

    @Autowired
    private SupplierClassificationService service;
/*

    @PostMapping(value = {"/add_category"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new category from the admin", notes = "Endpoint to add a new category for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the category"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<UsersCategory> add_category(@ModelAttribute UsersCategoryRequest request) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentuser == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);

        if (request.getName() == null)
            return new ResponseEntity("Name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getCategoryAssignedRole() == null)
            return new ResponseEntity("category's role can't be null", HttpStatus.BAD_REQUEST);
        UsersCategory category = new UsersCategory();
        category.setName(request.getName());
        category.setCategoryrole(request.getCategoryAssignedRole());
        if (request.getDescription() != null)
            category.setDescription(request.getDescription());
        Set<users> users = new HashSet<>();
        if (!request.getUsersIds().isEmpty()) {
            for (Long userId : request.getUsersIds()) {
                users user = this.userService.findById(userId);
                if (user == null)
                    return new ResponseEntity("user with id: " + userId + "  is not found", HttpStatus.NOT_FOUND);
                category.addUser(user);
            }
        }
        UsersCategory result = this.service.Save(category);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/update_category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Update an existing category", notes = "Endpoint to update an existing category for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the category"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Category not found")
    })
    public ResponseEntity<UsersCategory> update_category(@PathVariable Long categoryId, @ModelAttribute UsersCategoryRequest request) {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        if (currentUser == null)
            return new ResponseEntity("Current user not found", HttpStatus.NOT_FOUND);

        UsersCategory category = this.service.FindById(categoryId);
        if (category == null)
            return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND);

        if (request.getName() != null)
            category.setName(request.getName());

        if (request.getDescription() != null)
            category.setDescription(request.getDescription());

        if (request.getCategoryAssignedRole() != null)
            category.setCategoryrole(request.getCategoryAssignedRole());


        if (!request.getUsersIds().isEmpty()) {
            for(users u : category.getUsers()) {
                // Remove old users from the category
                category.removeUser(u);
            }
            // Add new users to the category
            for (Long userId : request.getUsersIds()) {
                users user = this.userService.findById(userId);
                if (user == null)
                    return new ResponseEntity("User with id: " + userId + " not found", HttpStatus.NOT_FOUND);
                category.addUser(user);
            }
        }

        UsersCategory result = this.service.Update(category);

        // Save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                currentUser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = {"/delete_category/{categoryId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "delete category by id for admin ", notes = "Endpoint to delete category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity delete_category(@PathVariable Long categoryId) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        UsersCategory result = this.service.FindById(categoryId);
        if (result == null)
            return new ResponseEntity("category with id: " + categoryId + " is not found", HttpStatus.NOT_FOUND);
        result.setArchive(true);
        this.service.Update(result);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
