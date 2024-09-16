package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Controller.Auth.AuthController;
import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.auth.CustomerSingUpRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.DepartmentService;
import com.camelsoft.rayaserver.Services.Project.PurshaseOrderService;
import com.camelsoft.rayaserver.Services.Project.RoleDepartmentService;
import com.camelsoft.rayaserver.Services.Project.SupplierClassificationService;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.PasswordResetTokenServices;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
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

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/sub_admin")
public class SubAdminController extends BaseController {
    private final Log logger = LogFactory.getLog(SubAdminController.class);

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
    private DepartmentService departmentService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    private SupplierClassificationService classificationService;

    @Autowired
    private PurshaseOrderService purshaseOrderService;


    @GetMapping(value = {"/search_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<DynamicResponse> search_admin(@RequestParam(defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean active) throws IOException {
        List<String> list = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "ROLE_SUB_ADMIN"));
        Page<users> user = this.criteriaService.UsersSearchCreatiriaRolesList(page, size, active, false, name, list );
        DynamicResponse ress = new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
        return new ResponseEntity<>(ress, HttpStatus.OK);
    }


    @GetMapping(value = {"/search_admin_list"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<List<users>> search_admin(@RequestParam(required = false) String name, @RequestParam(required = false) Boolean active) throws IOException {
        List<String> list = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "ROLE_SUB_ADMIN"));
        List<users> userList = this.criteriaService.UsersSearchCreatiriaRolesListNotPaginated(active, false, name, list);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_sub_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<users> add_sub_admin(@RequestBody CustomerSingUpRequest request, @RequestParam(required = false, name = "file") MultipartFile file) throws IOException, InterruptedException, MessagingException {
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
        if (request.getIddepartment() == null)
            return new ResponseEntity("iddepartment", HttpStatus.BAD_REQUEST);
        if (request.getIdroledepartment() == null)
            return new ResponseEntity("idroledepartment", HttpStatus.BAD_REQUEST);

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
        //check department and roledepartment
        Department department = this.departmentService.FindById(request.getIddepartment());
        if(department == null)
            return new ResponseEntity("department not founded using this is : " + request.getIddepartment(), HttpStatus.NOT_FOUND);
        RoleDepartment roledep = this.roleDepartmentService.FindById(request.getIdroledepartment());
        if(roledep == null)
            return new ResponseEntity("role department not founded using this is : "+ request.getIdroledepartment(), HttpStatus.NOT_FOUND);
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
        if (request.getInformationRequest().getGender() != null) {
            try {
                Gender gender = Gender.valueOf(request.getInformationRequest().getGender().toUpperCase());
                information.setGender(gender);
            } catch (IllegalArgumentException e) {
                // Handle invalid gender value if necessary
                // For example, you could log the error or set a default value
                logger.error(e.getMessage()+" | "+request.getInformationRequest().getGender());
            }
        }

        if (request.getInformationRequest().getWorksector() != null)
            information.setWorksector(WorkSector.valueOf(request.getInformationRequest().getWorksector()));
        if (request.getInformationRequest().getMaritalstatus() != null) {
            try {
                MaritalStatus statusmat = MaritalStatus.valueOf(request.getInformationRequest().getMaritalstatus().toUpperCase());
                information.setMaritalstatus(statusmat);
            } catch (IllegalArgumentException e) {
                // Handle invalid gender value if necessary
                // For example, you could log the error or set a default value
                logger.error(e.getMessage()+" | "+request.getInformationRequest().getMaritalstatus());
            }
        }
           // Set user details
        PersonalInformation resultinformation = this.personalInformationService.save(information);
        user.setUsername(username);
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(request.getPassword());
        user.setPersonalinformation(resultinformation);
        user.setDepartment(department);
        user.setRoledepartment(roledep);
        if(file != null){
            if (!this.filesStorageService.checkformat(file))
                return new ResponseEntity("this type is not acceptable : ", HttpStatus.NOT_ACCEPTABLE);
            MediaModel resource_media = filesStorageService.save_file(file, "profile");
            if (resource_media == null)
                return new ResponseEntity("error saving file", HttpStatus.NOT_IMPLEMENTED);
            user.setProfileimage(resource_media);
        }
        // add classification to sub-admin if founded idclassification
        if(request.getIdclassification() != null){
            SuppliersClassification classification = this.classificationService.FindById(request.getIdclassification());
            if(classification == null)
                return new ResponseEntity("classification not found with id: " + request.getIdclassification(), HttpStatus.NOT_FOUND);
            user.setSubadminClassification(classification);
        }
        // Save the user
        users result = userService.saveSubAdmin(user);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.CUSTOMER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //api to update or delete sub admin classification
    @PatchMapping(value = {"/update_sub_admin_classification/{sub_admin_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<users> add_sub_admin(@PathVariable Long sub_admin_id , @RequestParam Long other_sub_admin_id, @RequestParam(required = false) Long classification_id) throws IOException, InterruptedException {
        users user = userService.findById(sub_admin_id);
        if(user == null)
            return new ResponseEntity("sub admin not found", HttpStatus.NOT_FOUND);
        users othersubadmin = userService.findById(other_sub_admin_id);
        if(user == null)
            return new ResponseEntity("other sub admin not found", HttpStatus.NOT_FOUND);

        if(classification_id == null){
            user.setSubadminClassification(null);
        }else{
            SuppliersClassification classresult = this.classificationService.FindById(classification_id);

            if (classresult == null)
                return new ResponseEntity("classification not found with id: " + classification_id, HttpStatus.NOT_FOUND);
            user.setSubadminClassification(classresult);
        }

        //update all po assigned to sub admin and set it to the other sub admin
        List<PurshaseOrder> polist = this.purshaseOrderService.getPoListByAssignedSubAdmin(user);
        for(PurshaseOrder p : polist){
            user.getPoassigned().remove(p);
            p.setSubadminassignedto(othersubadmin);
            othersubadmin.getPoassigned().add(p);
            this.purshaseOrderService.Update(p);
        }
        if(othersubadmin.getSubadminClassification() == null){
            othersubadmin.setSubadminClassification(user.getSubadminClassification());
            this.userService.UpdateUser(othersubadmin);
        }

        user =  userService.UpdateUser(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

}
