package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Models.Auth.PasswordResetToken;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.User.SignupRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.PasswordResetTokenServices;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.camelsoft.rayaserver.Controller.Auth.AuthController.generateRandomResetCode;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/sub_supplier")
public class SubSupplierController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetTokenServices resetTokenServices;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private CriteriaService criteriaService;




    @GetMapping(value = {"/search_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') and hasAuthority('SUB_SUPPLIER_READ')")
    public ResponseEntity<DynamicResponse> search_admin(@RequestParam(defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean active) throws IOException {
        List<String> list = new ArrayList<>(Arrays.asList("ROLE_SUPPLIER", "ROLE_SUB_SUPPLIER"));
        Page<users> user = this.criteriaService.UsersSearchCreatiriaRolesList(page, size, active, false, name, list );
        DynamicResponse ress = new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
        return new ResponseEntity<>(ress, HttpStatus.OK);
    }


    @PostMapping(value = {"/add_sub_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') and hasAuthority('SUB_SUPPLIER_WRITE')")
    public ResponseEntity<users> add_sub_admin(@ModelAttribute SignupRequest request, @RequestParam List<Long> idList) throws IOException, InterruptedException, MessagingException {
        users usersList = this.userService.findTop();
        Long lastuserid = usersList.getId() + 1;
        String token = generateRandomResetCode(4);
        if (request.getEmail() == null && !request.getEmail().contains(" "))
            return new ResponseEntity("null email", HttpStatus.CONFLICT);
        if (!UserService.isValidEmail(request.getEmail()))
            return new ResponseEntity("data missing", HttpStatus.CONFLICT);
        users user = userService.findbyemail(request.getEmail().toLowerCase());
        if (user != null && user.getVerified())
            return new ResponseEntity("Account already exist.", HttpStatus.CONFLICT);
        String username = userService.GenerateUserName(request.getName(), lastuserid);

        if (request.getPhonenumber() == null)
            return new ResponseEntity("phone-number is null", HttpStatus.BAD_REQUEST);
        String phoneNUmber = request.getPhonenumber().replaceAll("[\\s()]", "");

        if (userService.existbyphonenumber(phoneNUmber))
            return new ResponseEntity("Phone number already exists.", HttpStatus.CONFLICT);

        user = new users(
                username,
                request.getEmail().toLowerCase(),
                request.getPassword(),
                request.getPhonenumber());
        user.setActive(true);
        user.setVerified(true);
        for (Long id : idList) {
            if (this.privilegeService.existbyid(id)) {
                Privilege privilege = this.privilegeService.findbyid(id);
                user.getPrivileges().add(privilege);
            }
        }
        users result = userService.saveSubSupplier(user);
        PersonalInformation pinfo = new PersonalInformation();
        pinfo.setGender(request.getGender());
        pinfo.setPhonenumber(request.getPhonenumber());
        pinfo.setBirthDate(request.getBirthDate());
        pinfo.setFirstnameen(request.getFirstnameen());
        pinfo.setLastnameen(request.getLastnameen());
        pinfo.setFirstnamear(request.getFirstnamear());
        pinfo.setLastnamear(request.getLastnamear());
        pinfo.setMaritalstatus(request.getMaritalstatus());
        pinfo.setUser(result);
        this.personalInformationService.save(pinfo);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(result);
        if (resetToken != null) {
            this.resetTokenServices.remove_code(resetToken.getUser());
        }
        this.resetTokenServices.createPasswordResetTokenForUser(result, token);
        /*CompletableFuture.runAsync(() -> {
            RestTemplate restTemplate = new RestTemplate();
             Send POST request
            restTemplate.exchange(url + "/api/v1/admin_sub_admin/sendConfirmEmailInEmailHtml?user_id=" + result.getId() + "&token=" + token, HttpMethod.POST, null, String.class);
        });*/

        return new ResponseEntity<>(result, HttpStatus.OK);
    }




}
