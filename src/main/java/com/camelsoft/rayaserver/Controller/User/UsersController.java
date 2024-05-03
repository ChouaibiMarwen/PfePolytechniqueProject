package com.camelsoft.rayaserver.Controller.User;


import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.SessionAction;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Tools.AddressRequest;
import com.camelsoft.rayaserver.Request.Tools.BankInformationRequest;
import com.camelsoft.rayaserver.Request.Tools.BillingAddressRequest;
import com.camelsoft.rayaserver.Request.User.LogOutRequest;
import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;

import com.camelsoft.rayaserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Tools.ApiResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;

import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.RoleService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.User.UserSessionService;
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

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/users")
public class UsersController extends BaseController {
    private final Log logger = LogFactory.getLog(UsersController.class);
    private static List<String> image_accepte_type = Arrays.asList("PNG", "png", "jpeg", "JPEG", "JPG", "jpg");
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

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_personal_information"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<PersonalInformation> update_personal_information(@ModelAttribute PersonalInformationRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
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
        if (request.getGrandfathernamear() != null) personalInformation.setGrandfathernamear(request.getGrandfathernamear());
        if (request.getSecondnameen() != null) personalInformation.setSecondnameen(request.getSecondnameen());
        if (request.getThirdnameen() != null) personalInformation.setThirdnameen(request.getThirdnameen());
        if (request.getGrandfathernameen() != null) personalInformation.setGrandfathernameen(request.getGrandfathernameen());
        if (request.getNumberofdependents() != null) personalInformation.setNumberofdependents(request.getNumberofdependents());
        if (request.getGender() != null) personalInformation.setGender(request.getGender());
        if (request.getWorksector() != null) personalInformation.setWorksector(request.getWorksector());
        if (request.getMaritalstatus() != null) personalInformation.setMaritalstatus(request.getMaritalstatus());
        PersonalInformation result = this.personalInformationService.update(personalInformation);



        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @PutMapping("/logout")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
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
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
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
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<users> get_user_by_id(@PathVariable Long userid) throws IOException {
        if (!this.userService.existbyid(userid))
            return new ResponseEntity("user not exist", HttpStatus.NOT_FOUND);
        users result = this.userService.findById(userid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


   /* @GetMapping(value= {"/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> getUserById(@PathVariable Long id) throws IOException {
        users result = this.userService.findById(id);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to fetch user", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }*/

    @GetMapping(value = {"/all"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all users by role and status for admin", notes = "Endpoint to get users")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<DynamicResponse> all(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5")  int size, @RequestParam String role,  @RequestParam(required = false) Boolean active, @RequestParam(required = false) String name , @RequestParam(required = false) Boolean verified) throws IOException {
        boolean exist = this.roleService.existsByRole(RoleEnum.valueOf(role));
        if(!exist)
            throw new ResourceNotFoundException("ROLE " + role + " Is Not Found");
        return new ResponseEntity<>(this.userService.filterAllUser(page, size, active, name, RoleEnum.valueOf(role), verified), HttpStatus.OK);
    }



    @PatchMapping(value = {"/verified/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update user verified to the opposit", notes = "Endpoint to update user's verified attribute")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the id supplier "),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Supllier not found with that id")
    })
    public ResponseEntity<users> updateUserVerification(@PathVariable Long id){
        users  user =  this.userService.updateVerifiedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = {"/add_Billing_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addUserBillingAddress(@PathVariable Long id,  @RequestBody BillingAddressRequest request) throws IOException, InterruptedException, MessagingException {
        users user = this.userService.findById(id);
        if (user == null) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        users updatedUser = this.userService.addBillingAddres(user, request);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = {"/add_Bank_account/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addUserBankAccount(@PathVariable Long id,  @RequestBody BankInformationRequest request) throws IOException, InterruptedException, MessagingException {
        users user = this.userService.findById(id);
        if (user == null) {
            return new ResponseEntity("Can't find user by that id", HttpStatus.CONFLICT);
        }
        users updatedUser = this.userService.addBankAccounToUser(user, request);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = {"/add_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add user address ", notes = "Endpoint to add address to user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Address> AddUserAddress(@PathVariable Long id,  @RequestBody AddressRequest request) throws IOException, InterruptedException, MessagingException {
        boolean exist = this.userService.existbyid(id);
        if (!exist) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        users user = this.userService.findById(id);
        Address result = this.userService.addAddressToUser(user, request);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to add  address", HttpStatus.CONFLICT);
        }
    }


    @PatchMapping(value = {"/activated/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update user activation to the opposit", notes = "Endpoint to update user's activate attribute")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the id supplier "),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Supllier not found with that id")
    })
    public ResponseEntity<users> updateUserActivation(@PathVariable Long id){
        users  user =  this.userService.updateActivatedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value= {"address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Set<Address>> getAddressUser(@PathVariable Long id) throws IOException {
        Set<Address> result = this.userService.getUserAddress(id);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to fetch user address", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @DeleteMapping(value = {"/{user_id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<users> daleteUserAddTimeStamp(@PathVariable Long user_id){
        users me = userService.findByUserName(getCurrentUser().getUsername());
        users user = this.userService.findById(user_id);
        if (!this.userService.existbyid(user_id))
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        if(user.getId() == me.getId()){
            return new ResponseEntity("cannot be delete this user", HttpStatus.BAD_REQUEST);

        }
        user = this.userService.deleteUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);


    }



}
