package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
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
@RequestMapping(value = "/api/v1/users_actions")
public class userActionsController extends BaseController {

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/last_actions_of_all_sub_admins"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get last actions of all sub admins for admin", notes = "Endpoint to getlast actions of all sub admins")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> last_actions_of_all_sub_admins(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("current user not found", HttpStatus.NOT_FOUND);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.AGENT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(this.userActionService.FindLastActionsForAllSubAdmins(page, size ), HttpStatus.OK);

    }


    @GetMapping(value = {"/user_historic_actions/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get last actions of all sub admins for admin", notes = "Endpoint to getlast actions of all sub admins")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> user_historic_actions(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = this.userService.findById(userId);
        if (user == null)
            return new ResponseEntity("current user not found", HttpStatus.NOT_FOUND);


        users current = userService.findByUserName(getCurrentUser().getUsername());
        if (current == null)
            return new ResponseEntity("current user not found", HttpStatus.NOT_FOUND);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.AGENT_MANAGEMENT,
                current
        );
        return new ResponseEntity<>(this.userActionService.FindAllSubAdminAction(page, size,user), HttpStatus.OK);

    }

    @GetMapping(value = {"/all_sub_admins_actions"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get last actions of all sub admins for admin", notes = "Endpoint to getlast actions of all sub admins")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_sub_admins_actions(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {


        users current = userService.findByUserName(getCurrentUser().getUsername());
        if (current == null)
            return new ResponseEntity("current user not found", HttpStatus.NOT_FOUND);

        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.AGENT_MANAGEMENT,
                current
        );
        return new ResponseEntity<>(this.userActionService.FindAllBySubAdmin(page, size), HttpStatus.OK);

    }

}
