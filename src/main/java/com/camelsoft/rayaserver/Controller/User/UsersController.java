package com.camelsoft.rayaserver.Controller.User;


import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.*;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Tools.AddressRequest;
import com.camelsoft.rayaserver.Request.Tools.BankInformationRequest;
import com.camelsoft.rayaserver.Request.Tools.BillingAddressRequest;
import com.camelsoft.rayaserver.Request.User.LogOutRequest;
import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;

import com.camelsoft.rayaserver.Request.User.UpdatePersonalInfoRequest;
import com.camelsoft.rayaserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Tools.ApiResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;

import com.camelsoft.rayaserver.Services.Project.DepartmentService;
import com.camelsoft.rayaserver.Services.Project.RoleDepartmentService;
import com.camelsoft.rayaserver.Services.Project.SupplierClassificationService;
import com.camelsoft.rayaserver.Services.Tools.AddressServices;
import com.camelsoft.rayaserver.Services.Tools.BankAccountService;
import com.camelsoft.rayaserver.Services.Tools.BillingAddressService;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.*;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Tools.Exception.ResourceNotFoundException;
import com.camelsoft.rayaserver.Tools.Exception.UserLogoutException;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/users")
public class UsersController extends BaseController {
    private final Log logger = LogFactory.getLog(UsersController.class);
    private static List<String> image_accepte_type = Arrays.asList("PNG", "png", "jpeg", "JPEG", "JPG", "jpg");

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PersonalInformationService personalInformationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BillingAddressService billingAddressService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private AddressServices addressServices;

    @Autowired
    private SupplierClassificationService classificationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleDepartmentService roleDepartmentService;
    @Autowired
    private SupplierServices supplierService;

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_current_user_personal_information"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    public ResponseEntity<PersonalInformation> update_personal_information(@ModelAttribute PersonalInformationRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        PersonalInformation personalInformation = new PersonalInformation();
        if (user.getPersonalinformation() != null) {
            personalInformation = user.getPersonalinformation();
        } else {
            personalInformation = this.personalInformationService.save(personalInformation);
            user.setPersonalinformation(personalInformation);
            this.userService.UpdateUser(user);
        }
        if (request.getFirstnameen() != null) personalInformation.setFirstnameen(request.getFirstnameen());
        if (request.getLastnameen() != null) personalInformation.setLastnameen(request.getLastnameen());
        if (request.getFirstnamear() != null) personalInformation.setFirstnamear(request.getFirstnamear());
        if (request.getLastnamear() != null) personalInformation.setLastnamear(request.getLastnamear());
        if (request.getBirthDate() != null) personalInformation.setBirthDate(request.getBirthDate());
        if (request.getSecondnamear() != null) personalInformation.setSecondnamear(request.getSecondnamear());
        if (request.getThirdnamear() != null) personalInformation.setThirdnamear(request.getThirdnamear());
        if (request.getPhonenumber() != null) personalInformation.setPhonenumber(request.getPhonenumber());
        if (request.getGrandfathernamear() != null)
            personalInformation.setGrandfathernamear(request.getGrandfathernamear());
        if (request.getSecondnameen() != null) personalInformation.setSecondnameen(request.getSecondnameen());
        if (request.getThirdnameen() != null) personalInformation.setThirdnameen(request.getThirdnameen());
        if (request.getGrandfathernameen() != null)
            personalInformation.setGrandfathernameen(request.getGrandfathernameen());
        if (request.getNumberofdependents() != null)
            personalInformation.setNumberofdependents(request.getNumberofdependents());
        if (request.getGender() != null) personalInformation.setGender(Gender.valueOf(request.getGender()));
        if (request.getWorksector() != null)
            personalInformation.setWorksector(WorkSector.valueOf(request.getWorksector()));
        if (request.getMaritalstatus() != null)
            personalInformation.setMaritalstatus(MaritalStatus.valueOf(request.getMaritalstatus()));
        PersonalInformation result = this.personalInformationService.update(personalInformation);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /*@PatchMapping(value = {"/update_user_personal_information/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    public ResponseEntity<PersonalInformation> updateUserPersonalInfo(@PathVariable Long userId , @RequestBody PersonalInformationRequest request) throws IOException {
        users user = this.userService.findById(userId);
        if(user == null ){
            return new ResponseEntity("User is not founded", HttpStatus.BAD_REQUEST);
        }
        PersonalInformation personalInformation = new PersonalInformation();
        if(user.getPersonalinformation()!=null) {
            personalInformation = user.getPersonalinformation();
        }else{
            personalInformation = this.personalInformationService.save(personalInformation);
            user.setPersonalinformation(personalInformation);
            this.userService.UpdateUser(user);
        }
        if (request.getFirstnameen() != null) personalInformation.setFirstnameen(request.getFirstnameen());
        if (request.getLastnameen() != null) personalInformation.setLastnameen(request.getLastnameen());
        if (request.getFirstnamear() != null) personalInformation.setFirstnamear(request.getFirstnamear());
        if (request.getLastnamear() != null) personalInformation.setLastnamear(request.getLastnamear());
        if (request.getBirthDate() != null) personalInformation.setBirthDate(request.getBirthDate());
        if (request.getSecondnamear() != null) personalInformation.setSecondnamear(request.getSecondnamear());
        if (request.getThirdnamear() != null) personalInformation.setThirdnamear(request.getThirdnamear());
        if (request.getPhonenumber() != null) personalInformation.setPhonenumber(request.getPhonenumber());
        if (request.getGrandfathernamear() != null) personalInformation.setGrandfathernamear(request.getGrandfathernamear());
        if (request.getSecondnameen() != null) personalInformation.setSecondnameen(request.getSecondnameen());
        if (request.getThirdnameen() != null) personalInformation.setThirdnameen(request.getThirdnameen());
        if (request.getGrandfathernameen() != null) personalInformation.setGrandfathernameen(request.getGrandfathernameen());
        if (request.getNumberofdependents() != null) personalInformation.setNumberofdependents(request.getNumberofdependents());
        if (request.getGender() != null) personalInformation.setGender(Gender.valueOf(request.getGender()));
        if (request.getWorksector() != null) personalInformation.setWorksector(WorkSector.valueOf(request.getWorksector()));
        if (request.getMaritalstatus() != null) personalInformation.setMaritalstatus(MaritalStatus.valueOf(request.getMaritalstatus()));
        PersonalInformation result = this.personalInformationService.update(personalInformation);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);


        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    @PatchMapping(value = {"/update_user_personal_information/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    public ResponseEntity<PersonalInformation> updateUserPersonalInfo(@PathVariable Long userId, @RequestBody UpdatePersonalInfoRequest request) throws IOException {
        users user = this.userService.findById(userId);
        if (user == null) {
            return new ResponseEntity("User is not founded", HttpStatus.BAD_REQUEST);
        }
        PersonalInformation information = user.getPersonalinformation();
        if (request.getPhonenumber() != null) {
            String phonenumber = request.getPhonenumber().replaceAll("[\\s()]", "");
            if(!Objects.equals(user.getPhonenumber(), request.getPhonenumber()) && userService.existbyphonenumber(phonenumber))
                return new ResponseEntity("phone-number", HttpStatus.CONFLICT);
            if(!Objects.equals(user.getPhonenumber(), request.getPhonenumber())){
                information.setPhonenumber(phonenumber);
                user.setPhonenumber(phonenumber);
            }

        }
        if(request.getVatNumber() != null)
            user.setVatnumber(request.getVatNumber());

        if (request.getInformationRequest() != null) {
            if (request.getInformationRequest().getFirstnameen() != null)
                information.setFirstnameen(request.getInformationRequest().getFirstnameen());
            if (request.getInformationRequest().getLastnameen() != null)
                information.setLastnameen(request.getInformationRequest().getLastnameen());
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

        }
        PersonalInformation result = this.personalInformationService.update(information);

        if (request.getSuppliernumber() != null || request.getCompanyName() != null || request.getIdnumber() != null || request.getIdType() != null || request.getIdclassification() != null) {
            if (!(user.getRole().getRole() == RoleEnum.ROLE_SUPPLIER || user.getRole().getRole() == RoleEnum.ROLE_SUB_SUPPLIER || user.getRole().getRole() == RoleEnum.ROLE_SUB_DEALER || user.getRole().getRole() == RoleEnum.ROLE_SUB_SUB_DEALER))
                return new ResponseEntity("this user is not a supplier", HttpStatus.BAD_REQUEST);
            Supplier supplier = user.getSupplier();
            if (request.getIdclassification() != null) {
                SuppliersClassification classresult = user.getSupplierclassification();
                if (classresult != null)
                    classresult.getSuppliers().remove(user);
                SuppliersClassification newclass = this.classificationService.FindById(request.getIdclassification());
                if (newclass == null)
                    return new ResponseEntity("no classififcation founded by that id ", HttpStatus.BAD_REQUEST);
                newclass.getSuppliers().add(user);
                user.setSupplierclassification(newclass);
            }
            if (request.getIdnumber() != null)
                supplier.setIdnumber(request.getIdnumber());

            if (request.getIdType() != null)
                supplier.setIdtype(request.getIdType());

            if (request.getCompanyName() != null) {
                supplier.setName(request.getCompanyName());
            }

            if (request.getSuppliernumber() != null) {
                Supplier supp = this.supplierService.findBySuppliernumber(request.getSuppliernumber());
                if (supp != null)
                    return new ResponseEntity("this supplier number is already exist : " + request.getSuppliernumber(), HttpStatus.BAD_REQUEST);
                supplier.setSuppliernumber(request.getSuppliernumber());

            }
            this.supplierService.update(supplier);
        }

        if (request.getIddepartment() != null) {
            Department department = this.departmentService.FindById(request.getIddepartment());
            if (department == null)
                return new ResponseEntity("department not founded using this is : " + request.getIddepartment(), HttpStatus.NOT_FOUND);
            user.setDepartment(department);
        }
        if (request.getIdroledepartment() != null) {
            RoleDepartment roledep = this.roleDepartmentService.FindById(request.getIdroledepartment());
            if (roledep == null)
                return new ResponseEntity("role department not founded using this is : " + request.getIdroledepartment(), HttpStatus.NOT_FOUND);
            user.setRoledepartment(roledep);
        }
        String name = result.getFirstnameen() + result.getLastnameen();
        String username = userService.GenerateUserName(name, userService.Count());
        user.setUsername(username);
        this.userService.UpdateUser(user);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);


        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/logout")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<ApiResponse> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        if (deviceId == null)
            return new ResponseEntity("device id not found", HttpStatus.BAD_REQUEST);

        if (!this.userDeviceService.existbytoken(logOutRequest.getToken()))
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);

        if (this.userDeviceService.findbytoken(logOutRequest.getToken()) == null)
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);


        UserDevice userDevice = this.userDeviceService.findbytoken(logOutRequest.getToken());
        if (userDevice != null && userDevice.getDeviceId().equals(deviceId)) {
            OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(), logOutRequest.getToken(), logOutRequest);
            applicationEventPublisher.publishEvent(logoutSuccessEvent);
            userDevice.setLogout(true);
            UserDevice deviceResult = this.userDeviceService.update(userDevice);
            UserSession session = new UserSession(currentUser, deviceResult, SessionAction.LOGOUT, "users Logout ");
            this.userSessionService.save(session);
            return ResponseEntity.ok(new ApiResponse(true, "users has successfully logged out from the system!"));
        } else {
            throw new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user ");
        }

    }

    @PatchMapping(value = {"/update_password"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity update_password(@RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword) throws IOException {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(currentUser.getUsername(), oldpassword)
        );
        if (!authentication.isAuthenticated())
            return new ResponseEntity("password mismatch", HttpStatus.CONFLICT);
        this.userService.updatepassword(currentUser, newpassword);
        return new ResponseEntity("password updated successfully", HttpStatus.OK);

    }


    @GetMapping(value = {"/get_user/{userid}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<users> get_user_by_id(@PathVariable Long userid) throws IOException {
        if (!this.userService.existbyid(userid))
            return new ResponseEntity("user not exist", HttpStatus.NOT_FOUND);
        users result = this.userService.findById(userid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/all"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "get all users by role and status for admin", notes = "Endpoint to get users")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<DynamicResponse> all(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam String role, @RequestParam(required = false) Boolean active, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean verified) throws IOException {
        boolean exist = this.roleService.existsByRole(RoleEnum.valueOf(role));
        if (!exist)
            throw new ResourceNotFoundException("ROLE " + role + " Is Not Found");
        return new ResponseEntity<>(this.userService.filterAllUser(page, size, active, name, RoleEnum.valueOf(role), verified), HttpStatus.OK);
    }


    @GetMapping(value = {"/all_users_list_by_role"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "get all users by role and status for admin", notes = "Endpoint to get users")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<List<UserShortDto>> all_users_list_by_role(@RequestParam RoleEnum role) throws IOException {
        List<users> user = null;
        user = this.userService.allusersByRole(role);
        List<UserShortDto> shortuser = user.stream().map(UserShortDto::mapToUserShortDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shortuser, HttpStatus.OK);
    }


    @GetMapping(value = {"/users_list"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "get all users' short form for admin", notes = "Endpoint to get users")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<List<UserShortDto>> users_list() throws IOException {
        List<users> user = null;
        user = this.userService.allusers();
        List<UserShortDto> shortuser = user.stream().map(UserShortDto::mapToUserShortDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shortuser, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_users_list_by_roles_list"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "get all users by role and status for admin", notes = "Endpoint to get users")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<List<UserShortDto>> all_users_list_by_roles_set(@RequestParam List<RoleEnum> role) throws IOException {
        List<users> user = null;
        user = this.userService.getUsersByRoles(role);
        List<UserShortDto> shortuser = user.stream().map(UserShortDto::mapToUserShortDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shortuser, HttpStatus.OK);
    }

    @PatchMapping(value = {"/verified/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "update user verified to the opposit", notes = "Endpoint to update user's verified attribute")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the id supplier "),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Supllier not found with that id")
    })
    public ResponseEntity<users> updateUserVerification(@PathVariable Long id) {
        users user = this.userService.updateVerifiedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = {"/add_Billing_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addUserBillingAddress(@PathVariable Long id, @RequestBody BillingAddressRequest request) throws IOException, InterruptedException, MessagingException {
        List<String> nullFields = new ArrayList<>();

        if (request.getEmail() == null) {
            nullFields.add("email");
        }
        if (request.getFirstname() == null) {
            nullFields.add("firstname");
        }
        if (request.getLastname() == null) {
            nullFields.add("lastname");
        }
        if (request.getBillingaddress() == null) {
            nullFields.add("billingaddress");
        }
        if (request.getZipcode() == null) {
            nullFields.add("zipcode");
        }
        if (request.getCity() == null) {
            nullFields.add("city");
        }
        if (request.getPhonenumber() == null) {
            nullFields.add("phonenumber");
        }
        // Check if any field is null
        if (!nullFields.isEmpty()) {
            String errorMessage;
            if (nullFields.size() == 1) {
                errorMessage = "Bad request, the following field is null: " + nullFields.get(0);
            } else {
                errorMessage = "Bad request, the following fields are null: " + String.join(", ", nullFields);
            }
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
        }

        users user = this.userService.findById(id);
        if (user == null) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        users updatedUser = this.userService.addBillingAddres(user, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BILLING_ADDRESS_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping(value = {"/update_Billing_Address/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Update Billing address", notes = "Endpoint to update billing address of a user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully updated"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, no attribute provided for update"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "billing address not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Failed to update billing address")
    })
    public ResponseEntity<BillingAddress> updateUserBillingAddress(@PathVariable Long userId, @RequestBody BillingAddressRequest request) throws IOException, InterruptedException, MessagingException {
        // Check if at least one attribute is provided for the update
        if (request.getEmail() == null && request.getFirstname() == null && request.getLastname() == null &&
                request.getBillingaddress() == null && request.getCountry() == null && request.getZipcode() == null &&
                request.getCity() == null && request.getState() == null && request.getPhonenumber() == null) {
            return new ResponseEntity("At least one attribute should be provided for update", HttpStatus.BAD_REQUEST);
        }
        users user = this.userService.findById(userId);
        if (user == null) {
            return new ResponseEntity("user is not found", HttpStatus.NOT_FOUND);
        }
        BillingAddress billingAddress = user.getBillingAddress();
        if (user.getBillingAddress() != null) {
            billingAddress = user.getBillingAddress();
        } else {
            billingAddress = this.billingAddressService.saveBiLLAddress(billingAddress);
            user.setBillingAddress(billingAddress);
            this.userService.UpdateUser(user);
        }

        billingAddress = this.billingAddressService.updateBillingAddress(billingAddress, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BILLING_ADDRESS_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (billingAddress != null) {
            return ResponseEntity.ok(billingAddress);
        } else {
            return new ResponseEntity("Failed to update billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"billing_Address/{billingId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<BillingAddress> getBillingAddress(@PathVariable Long billingId) throws IOException {
        BillingAddress billingAddress = this.billingAddressService.findBillingAddressById(billingId);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BILLING_ADDRESS_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (billingAddress != null) {
            return ResponseEntity.ok(billingAddress);
        } else {
            return new ResponseEntity("Failed to fetch billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @PostMapping(value = {"/add_Bank_account/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addUserBankAccount(@PathVariable Long id, @RequestBody BankInformationRequest request ,  @RequestParam(value = "file", required = false) MultipartFile ibanattachment ) throws IOException, InterruptedException, MessagingException {

        List<String> nullFields = new ArrayList<>();

        if (request.getBank_name() == null ) {
            nullFields.add("bank_name");
        }
        if (request.getAccountHolderName() == null ) {
            nullFields.add("accountHolderName");
        }
        if (request.getAcountNumber() == null ) {
            nullFields.add("acountNumber");
        }

        // Check if any required field is null or empty
        if (!nullFields.isEmpty()) {
            String errorMessage;
            if (nullFields.size() == 1) {
                errorMessage = "Bad request, the following field is null or empty: " + nullFields.get(0);
            } else {
                errorMessage = "Bad request, the following fields are null or empty: " + String.join(", ", nullFields);
            }
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
        }

        users user = this.userService.findById(id);
        if (user == null) {
            return new ResponseEntity("Can't find user by that id", HttpStatus.BAD_REQUEST);
        }
        BankInformation b = this.userService.addBankAccounToUser(user, request, ibanattachment);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BANK_ACCOUNT_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (b != null) {
            return ResponseEntity.ok(this.userService.findById(id));
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping(value = {"/update_Bank_account/{userId}/{bankInfoId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Update Bank Account", notes = "Endpoint to update bank account of a user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully updated"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, at least one attribute should be provided"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Bank information not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Failed to update bank account")
    })
    public ResponseEntity<BankInformation> updateUserBankAccount(@PathVariable Long userId, @PathVariable Long bankInfoId, @RequestBody BankInformationRequest request) throws IOException, InterruptedException, MessagingException {
        // Check if at least one field is provided in the request
        if (request.getBank_name() == null && request.getAccountHolderName() == null && request.getAcountNumber() == null && request.getIban() == null) {
            String errorMessage = "At least one attribute should be provided for bank account update";
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
        }
        users user = this.userService.findById(userId);
        if (user == null) {
            return new ResponseEntity("user is not found", HttpStatus.NOT_FOUND);
        }
        BankInformation bankInformation = this.bankAccountService.findByIdWithoutException(bankInfoId);

        // Get the bank information by id
        if (bankInformation == null) {
            BankInformation bankInformation1 = new BankInformation();
            bankInformation1.setUser(user);
            bankInformation = this.bankAccountService.saveBankInformation(bankInformation1);
        }

        bankInformation = this.bankAccountService.updateBankInfo(bankInformation, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BANK_ACCOUNT_MANAGEMENT,
                currentuser
        );
        if (bankInformation != null) {
            return ResponseEntity.ok(bankInformation);
        } else {
            return new ResponseEntity("Failed to update billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"bank_information/{bankInformationId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<BankInformation> getBankInformation(@PathVariable Long bankInformationId) throws IOException {
        BankInformation bankInformation = this.bankAccountService.findById(bankInformationId);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BANK_ACCOUNT_MANAGEMENT,
                currentuser
        );
        if (bankInformation != null) {
            return ResponseEntity.ok(bankInformation);
        } else {
            return new ResponseEntity("Failed to fetch bank account", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping(value = {"/add_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add user address ", notes = "Endpoint to add address to user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Address> AddUserAddress(@PathVariable Long id, @RequestBody AddressRequest request) throws IOException, InterruptedException, MessagingException {

        List<String> nullFields = new ArrayList<>();

        if (request.getAddressline1() == null || request.getAddressline1().isEmpty()) {
            nullFields.add("addressline1");
        }
        if (request.getPostcode() == null || request.getPostcode().isEmpty()) {
            nullFields.add("postcode");
        }
        if (request.getCountryName() == null || request.getCountryName().isEmpty()) {
            nullFields.add("countryName");
        }
        if (request.getCityName() == null || request.getCityName().isEmpty()) {
            nullFields.add("cityName");
        }
        if (request.getPrimaryaddress() == null) {
            nullFields.add("primaryaddress");
        }

        // Check if any field is null
        if (!nullFields.isEmpty()) {
            String errorMessage;
            if (nullFields.size() == 1) {
                errorMessage = "Bad request, the following field is null: " + nullFields.get(0);
            } else {
                errorMessage = "Bad request, the following fields are null: " + String.join(", ", nullFields);
            }
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
        }

        boolean exist = this.userService.existbyid(id);
        if (!exist) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        users user = this.userService.findById(id);
        Address result = this.userService.addAddressToUser(user, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to add  address", HttpStatus.CONFLICT);
        }
    }


    @PatchMapping(value = {"/update_Address/{userId}/{addressId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Update user address", notes = "Endpoint to update user address")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully updated"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, at least one attribute should be provided"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "User not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Failed to update address")
    })
    public ResponseEntity<Address> updateUserAddress(@PathVariable Long userId, @PathVariable Long addressId, @RequestBody AddressRequest request) throws IOException, InterruptedException, MessagingException {
        // Check if at least one field is provided in the request
        if (request.getAddressline1() == null && request.getAddressline2() == null &&
                request.getPostcode() == null && request.getBuilding() == null &&
                request.getUnitnumber() == null && request.getStreetname() == null &&
                request.getPrimaryaddress() == null && request.getCountryName() == null &&
                request.getCityName() == null) {
            String errorMessage = "At least one attribute should be provided for updating address";
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);

        }

        // Check if the user exists
        users user = this.userService.findById(userId);
        if (user == null) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }


        Address address = this.addressServices.findByIdWithoutEXCEPTION(addressId);

        // Get the bank information by id
        if (address == null) {
            Address address1 = new Address();
            address1.setUser(user);
            address = this.addressServices.save(address1);
        }

        // Update the address of the user
        Address result = this.addressServices.updateAddress(address, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to update address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = {"/activated/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "update user activation to the opposit", notes = "Endpoint to update user's activate attribute")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the id supplier "),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Supllier not found with that id")
    })
    public ResponseEntity<users> updateUserActivation(@PathVariable Long id) {
        users user = this.userService.updateActivatedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value = {"address/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Address> getAddressUser(@PathVariable Long id) throws IOException {
        Address result = this.addressServices.findById(id);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to fetch address", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @DeleteMapping(value = {"/{user_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<users> daleteUserAddTimeStamp(@PathVariable Long user_id) {
        users me = userService.findByUserName(getCurrentUser().getUsername());
        users user = this.userService.findById(user_id);
        if (!this.userService.existbyid(user_id))
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        if (user.getId() == me.getId()) {
            return new ResponseEntity("cannot be delete this user", HttpStatus.BAD_REQUEST);

        }
        user = this.userService.deleteUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);


    }


    @DeleteMapping(value = {"bank_information/{bankInformationId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully deleted Bank Information"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<String> deleteBankInformation(@PathVariable Long bankInformationId) throws IOException {
        BankInformation bankInformation = this.bankAccountService.findById(bankInformationId);
        this.bankAccountService.deleteBankInformation(bankInformation);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.BANK_ACCOUNT_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>("Bank Information deleted successfully", HttpStatus.OK);
    }


    @DeleteMapping(value = {"address/{addressId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully deleted Bank Information"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) throws IOException {
        Address Address = this.addressServices.findById(addressId);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        this.addressServices.deleteAddress(Address);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }


}
