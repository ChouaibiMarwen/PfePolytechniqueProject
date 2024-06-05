package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Auth.PasswordResetToken;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.User.SignupRequest;
import com.camelsoft.rayaserver.Request.auth.CustomerSingUpRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.PasswordResetTokenServices;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private UserActionService userActionService;
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

    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private SupplierServices supplierServices;

    @GetMapping(value = {"/search_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<DynamicResponse> search_supplier(@RequestParam(defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean active) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        List<String> list = new ArrayList<>(Arrays.asList("ROLE_SUPPLIER", "ROLE_SUB_SUPPLIER"));
        Page<users> user = this.criteriaService.UsersSearchCreatiriaRolesListsupplier(page, size, active, false, name, list,currentuser );
        DynamicResponse ress = new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
        return new ResponseEntity<>(ress, HttpStatus.OK);
    }
    @GetMapping(value = {"/all"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all suppliers without pagination", notes = "Endpoint to get suppliers")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<List<UserShortDto>> all(@RequestParam(required = false) Boolean active, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean verified) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        List<UserShortDto> result = new ArrayList<>() ;
        if(currentuser.getManager() == null && currentuser.getRole().getRole()==RoleEnum.ROLE_SUPPLIER){
            result =  this.supplierServices.getAllSubSupplierWithoutPaginationSupplier(active, name, RoleEnum.ROLE_SUPPLIER, verified,currentuser);
        }else{
            // need to adjust
            result =  this.supplierServices.getAllSubSupplierWithoutPaginationSupplier(active, name, RoleEnum.ROLE_SUPPLIER, verified,currentuser);

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }





    @PostMapping(value = {"/add_sub_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')")
    public ResponseEntity<users> add_sub_admin(@RequestBody CustomerSingUpRequest request, @RequestParam(required = false, name = "file") MultipartFile file) throws IOException, InterruptedException, MessagingException {

        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        // Check if email is null
        if (request.getEmail() == null)
            return new ResponseEntity("email", HttpStatus.BAD_REQUEST);
        if (request.getPhonenumber() == null)
            return new ResponseEntity("phone-number", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getFirstnameen() == null)
            return new ResponseEntity("first-name-en", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getFirstnamear() == null)
            return new ResponseEntity("first-name-ar", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getLastnameen() == null)
            return new ResponseEntity("last-name-en", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getLastnamear() == null)
            return new ResponseEntity("last-name-ar", HttpStatus.BAD_REQUEST);

        // Check email format
        if (!UserService.isValidEmail(request.getEmail().toLowerCase()) && !request.getEmail().contains(" "))
            return new ResponseEntity("email", HttpStatus.NOT_ACCEPTABLE);
        String phonenumber = request.getPhonenumber().replaceAll("[\\s()]", "");
        if (userService.existbyphonenumber(phonenumber))
            return new ResponseEntity("phone-number", HttpStatus.CONFLICT);
        if (userService.existbyemail(request.getEmail().toLowerCase()))
            return new ResponseEntity("email", HttpStatus.CONFLICT);
        String name = request.getInformationRequest().getFirstnameen() + request.getInformationRequest().getLastnameen();
        String username = userService.GenerateUserName(name, userService.Count());
        users existingUserByUsername = userService.findByUserName(username);
        if (existingUserByUsername != null)
            return new ResponseEntity("user-name", HttpStatus.CONFLICT);
        // Create a new user
        users user = new users();
        PersonalInformation information = new PersonalInformation();
        information.setPhonenumber(phonenumber);
        information.setFirstnameen(request.getInformationRequest().getFirstnameen());
        information.setLastnameen(request.getInformationRequest().getLastnameen());
        information.setFirstnamear(request.getInformationRequest().getLastnamear());
        information.setLastnamear(request.getInformationRequest().getLastnamear());
        if (request.getInformationRequest().getBirthDate() != null)
            information.setBirthDate(request.getInformationRequest().getBirthDate());
        if (request.getInformationRequest().getSecondnameen() != null)
            information.setSecondnameen(request.getInformationRequest().getSecondnameen());
        if (request.getInformationRequest().getThirdnameen() != null)
            information.setThirdnameen(request.getInformationRequest().getThirdnameen());
        if (request.getInformationRequest().getGrandfathernameen() != null)
            information.setGrandfathernameen(request.getInformationRequest().getGrandfathernameen());
        if (request.getInformationRequest().getSecondnamear() != null)
            information.setSecondnamear(request.getInformationRequest().getSecondnamear());
        if (request.getInformationRequest().getThirdnamear() != null)
            information.setThirdnamear(request.getInformationRequest().getThirdnamear());
        if (request.getInformationRequest().getGrandfathernamear() != null)
            information.setGrandfathernamear(request.getInformationRequest().getGrandfathernamear());
        if (request.getInformationRequest().getNumberofdependents() != null)
            information.setNumberofdependents(request.getInformationRequest().getNumberofdependents());
        if (request.getInformationRequest().getGender() != null)
            information.setGender(Gender.valueOf(request.getInformationRequest().getGender()));
        if (request.getInformationRequest().getWorksector() != null)
            information.setWorksector(WorkSector.valueOf(request.getInformationRequest().getWorksector()));
        if (request.getInformationRequest().getMaritalstatus() != null)
            information.setMaritalstatus(MaritalStatus.valueOf(request.getInformationRequest().getMaritalstatus()));
        // Set user details
        PersonalInformation resultinformation = this.personalInformationService.save(information);
        user.setUsername(username);
        if(currentuser.getRole().getRole() == RoleEnum.ROLE_SUPPLIER )
            user.setManager(currentuser);
        if(currentuser.getManager() != null && currentuser.getManager().getRole().getRole()== RoleEnum.ROLE_SUB_SUPPLIER) {
            user.setManager(currentuser.getManager());
        }else if(currentuser.getManager()!= null && currentuser.getManager().getRole().getRole()== RoleEnum.ROLE_SUPPLIER){
            user.setManager(currentuser);

        }else{
            return new ResponseEntity("you're not a supplier or you don't have manager : ", HttpStatus.NOT_ACCEPTABLE);

        }
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(request.getPassword());
        user.setPersonalinformation(resultinformation);
        if (!this.filesStorageService.checkformat(file))
            return new ResponseEntity("this type is not acceptable : ", HttpStatus.NOT_ACCEPTABLE);
        File_model resource_media = filesStorageService.save_file_local(file, "profile");
        if (resource_media == null)
            return new ResponseEntity("error saving file", HttpStatus.NOT_IMPLEMENTED);
        user.setProfileimage(resource_media);
        // Save the user
        users result = userService.saveSubSupplier(user);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }




}
