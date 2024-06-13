package com.camelsoft.rayaserver.Controller.Auth;

import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.PasswordResetTokenServices;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/privileges")
public class PrivilegeController  extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;


    @GetMapping(value = {"/get_all_privileges"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all privileges for admin", notes = "Endpoint to get all privileges for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Privilege>> get_all_privileges() throws IOException, InterruptedException, MessagingException {
        List<Privilege> privileges = this.privilegeService.findAll();
        return new ResponseEntity<>(privileges, HttpStatus.OK);
    }

    @PatchMapping(value = {"/add_user_privileges/{user_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<users> add_sub_admin_privileges(@PathVariable Long user_id, @RequestParam List<Long> idList) throws IOException, InterruptedException, MessagingException {

        if (!this.userService.existbyid(user_id))
            return new ResponseEntity("invalid id" + user_id, HttpStatus.NOT_FOUND);
        users user = userService.findById(user_id);
        for (Long id : idList) {
            if (this.privilegeService.existbyid(id)) {
                Privilege privilege = this.privilegeService.findbyid(id);
                if(privilege == null)
                    return new ResponseEntity("privilege is not found in our system", HttpStatus.NOT_FOUND);
                user.getPrivileges().add(privilege);
            }
        }
        users result = userService.UpdateUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/remove_user_privileges/{user_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<users> remove_sub_admin_privileges(@PathVariable Long user_id, @RequestParam List<Long> idList) throws IOException, InterruptedException, MessagingException {
        if (!this.userService.existbyid(user_id))
            return new ResponseEntity("invalid id" + user_id, HttpStatus.NOT_FOUND);
        users user = userService.findById(user_id);
        for (Long id : idList) {
            if (this.privilegeService.existbyid(id)) {
                Privilege privilege = this.privilegeService.findbyid(id);
                if(privilege == null)
                    return new ResponseEntity("privilege is not found in our system", HttpStatus.NOT_FOUND);
                user.getPrivileges().remove(privilege);
            }
        }
        users result = userService.UpdateUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }





}
