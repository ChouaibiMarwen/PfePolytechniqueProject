package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
import com.camelsoft.rayaserver.Request.project.UsersCategoryRequest;
import com.camelsoft.rayaserver.Services.Project.UserCategoryService;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/users_categories")
public class UsersCategoryController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private UserCategoryService service;



    @GetMapping(value = {"/all_categories_by_name"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all categories for admin by name", notes = "Endpoint to get categories by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<UsersCategory>> all_categories_by_name(@RequestParam String name) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        List<UsersCategory> result = this.service.FindAllByNameList(name);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_categories_assigned_role"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all categories for admin by assigned role", notes = "Endpoint to get categories by assigned role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<UsersCategory>> all_categories_assigned_role(@RequestParam RoleEnum role) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        List<UsersCategory> result = this.service.FindAllByCategoryRoleList(role);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/user_assigned_categories_list/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all user's categories for admin by assigned role", notes = "Endpoint to get all user's categories for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<UsersCategory>> user_assigned_categories_List(@RequestParam Long userId) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        List<UsersCategory> result = this.service.getCategoriesForUserList(userId);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @GetMapping(value = {"/category/{categoryId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get category by id for admin ", notes = "Endpoint to get category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<UsersCategory> getCategoryById(@PathVariable Long categoryId) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        UsersCategory result = this.service.FindById(categoryId);
        if (result == null)
            return new ResponseEntity("category with id: " + categoryId + " is not found", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/users_list_of_category/{categoryId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get users list category by id for admin ", notes = "Endpoint to get users list category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Set<users>> getUsersListOfCategory(@PathVariable Long categoryId) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        UsersCategory result = this.service.FindById(categoryId);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        Set<users> userset = result.getUsers();
        return new ResponseEntity<>(userset, HttpStatus.OK);
    }




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

        UsersCategory  category = new UsersCategory();
        category.setName(request.getName());
        category.setCategoryrole(request.getCategoryAssignedRole());
        if(request.getDescription()!= null)
            category.setDescription(request.getDescription());
        if(!request.getUsersIds().isEmpty()){
            for(Long userId : request.getUsersIds()){
                users user = this.userService.findById(userId);
                if (user == null)
                    return new ResponseEntity("user with id: " + userId + "  is not found", HttpStatus.NOT_FOUND);

                category.getUsers().add(user);
            }
        }
        UsersCategory result = this.service.Save(category);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.EVENT_MANAGEMENT,
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

        // Remove old users from the category
        category.getUsers().clear();

        // Add new users to the category
        if (!request.getUsersIds().isEmpty()) {
            for (Long userId : request.getUsersIds()) {
                users user = this.userService.findById(userId);
                if (user == null)
                    return new ResponseEntity("User with id: " + userId + " not found", HttpStatus.NOT_FOUND);

                category.getUsers().add(user);
                user.getCategories().add(category);
            }
        }

        UsersCategory result = this.service.Save(category);

        // Save new action
        UserAction action = new UserAction(
                UserActionsEnum.EVENT_MANAGEMENT,
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
            return new ResponseEntity("category with id: "+ categoryId +" is not found", HttpStatus.NOT_FOUND);
         result.setArchive(true);
        this.service.Update(result);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.USERS_CATEGORIES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>( HttpStatus.OK);
    }



}
