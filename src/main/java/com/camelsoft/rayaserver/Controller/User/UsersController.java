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
import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
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

import com.camelsoft.rayaserver.Services.Tools.AddressServices;
import com.camelsoft.rayaserver.Services.Tools.BankAccountService;
import com.camelsoft.rayaserver.Services.Tools.BillingAddressService;
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
    @Autowired
    private BillingAddressService billingAddressService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private AddressServices addressServices;

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') ")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_current_user_personal_information"})
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


    @PatchMapping(value = {"/update_user_personal_information/{userId}"})
    @PreAuthorize("hasRole('ADMIN')  or hasRole('SUPPLIER') ")
    public ResponseEntity<PersonalInformation> updateUserPersonalInfo(@PathVariable Long userId , @ModelAttribute PersonalInformationRequest request) throws IOException {
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
        List<String> nullFields = new ArrayList<>();

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            nullFields.add("email");
        }
        if (request.getFirstname() == null || request.getFirstname().isEmpty()) {
            nullFields.add("firstname");
        }
        if (request.getLastname() == null || request.getLastname().isEmpty()) {
            nullFields.add("lastname");
        }
        if (request.getBillingaddress() == null || request.getBillingaddress().isEmpty()) {
            nullFields.add("billingaddress");
        }
        if (request.getCountry() == null || request.getCountry().isEmpty()) {
            nullFields.add("country");
        }
        if (request.getZipcode() == null || request.getZipcode().isEmpty()) {
            nullFields.add("zipcode");
        }
        if (request.getCity() == null || request.getCity().isEmpty()) {
            nullFields.add("city");
        }
        if (request.getState() == null || request.getState().isEmpty()) {
            nullFields.add("state");
        }
        if (request.getPhonenumber() == null || request.getPhonenumber().isEmpty()) {
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
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PatchMapping(value = {"/update_Billing_Address/{userId}"})
    @PreAuthorize("hasRole('ADMIN')")
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
                request.getCity() == null && request.getState() == null && request.getPhonenumber() == null)  {
            return new ResponseEntity("At least one attribute should be provided for update", HttpStatus.BAD_REQUEST);
        }
        users user = this.userService.findById(userId);
        if (user == null) {
            return new ResponseEntity("user is not found", HttpStatus.NOT_FOUND);
        }
        BillingAddress billingAddress = user.getBillingAddress();
        if(user.getBillingAddress()!=null) {
            billingAddress = user.getBillingAddress();
        }else{
            billingAddress = this.billingAddressService.saveBiLLAddress(billingAddress);
            user.setBillingAddress(billingAddress);
            this.userService.UpdateUser(user);
        }

        billingAddress = this.billingAddressService.updateBillingAddress(billingAddress, request);

        if (billingAddress != null) {
            return ResponseEntity.ok(billingAddress);
        } else {
            return new ResponseEntity("Failed to update billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value= {"billing_Address/{billingId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<BillingAddress> getBillingAddress(@PathVariable Long billingId) throws IOException {
       BillingAddress billingAddress = this.billingAddressService.findBillingAddressById(billingId);

        if (billingAddress != null) {
            return ResponseEntity.ok(billingAddress);
        } else {
            return new ResponseEntity("Failed to fetch billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @PostMapping(value = {"/add_Bank_account/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addUserBankAccount(@PathVariable Long id,  @RequestBody BankInformationRequest request) throws IOException, InterruptedException, MessagingException {

        List<String> nullFields = new ArrayList<>();

        if (request.getBank_name() == null || request.getBank_name().isEmpty()) {
            nullFields.add("bank_name");
        }
        if (request.getAccountHolderName() == null || request.getAccountHolderName().isEmpty()) {
            nullFields.add("accountHolderName");
        }
        if (request.getAcountNumber() == null || request.getAcountNumber().isEmpty()) {
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
        BankInformation  b =  this.userService.addBankAccounToUser(user, request);
        if (b != null) {
            return ResponseEntity.ok(this.userService.findById(id));
        } else {
            return new ResponseEntity("Failed to add billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping(value = {"/update_Bank_account/{userId}/{bankInfoId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update Bank Account", notes = "Endpoint to update bank account of a user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully updated"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, at least one attribute should be provided"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Bank information not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Failed to update bank account")
    })
    public ResponseEntity<BankInformation> updateUserBankAccount(@PathVariable Long userId ,@PathVariable Long bankInfoId, @RequestBody BankInformationRequest request) throws IOException, InterruptedException, MessagingException {
        // Check if at least one field is provided in the request
        if (request.getBank_name() == null && request.getAccountHolderName() == null && request.getAcountNumber() == null && request.getIBAN() == null) {
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

        if (bankInformation != null) {
            return ResponseEntity.ok(bankInformation);
        } else {
            return new ResponseEntity("Failed to update billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value= {"bank_information/{bankInformationId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<BankInformation> getBankInformation(@PathVariable Long bankInformationId) throws IOException {
        BankInformation bankInformation = this.bankAccountService.findById(bankInformationId);

        if (bankInformation != null) {
            return ResponseEntity.ok(bankInformation);
        } else {
            return new ResponseEntity("Failed to fetch bank account", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping(value = {"/add_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add user address ", notes = "Endpoint to add address to user")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully add"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, check the data is null"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Address> AddUserAddress(@PathVariable Long id,  @RequestBody AddressRequest request) throws IOException, InterruptedException, MessagingException {

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
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to add  address", HttpStatus.CONFLICT);
        }
    }



    @PatchMapping(value = {"/update_Address/{userId}/{addressId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update user address", notes = "Endpoint to update user address")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully updated"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, at least one attribute should be provided"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "User not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Failed to update address")
    })
    public ResponseEntity<Address> updateUserAddress(@PathVariable Long userId,  @PathVariable Long addressId, @RequestBody AddressRequest request) throws IOException, InterruptedException, MessagingException {
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
        if (user == null ){
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
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to update address", HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<Address> getAddressUser(@PathVariable Long id) throws IOException {
        Address result = this.addressServices.findById(id);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to fetch address", HttpStatus.INTERNAL_SERVER_ERROR);
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


    @DeleteMapping(value = {"bank_information/{bankInformationId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully deleted Bank Information"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<String> deleteBankInformation(@PathVariable Long bankInformationId) throws IOException{
        BankInformation bankInformation = this.bankAccountService.findById(bankInformationId);
        this.bankAccountService.deleteBankInformation(bankInformation);
        return new ResponseEntity<>("Bank Information deleted successfully", HttpStatus.OK);
    }



    @DeleteMapping(value = {"address/{addressId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully deleted Bank Information"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) throws IOException{
        Address Address = this.addressServices.findById(addressId);
        this.addressServices.deleteAddress(Address);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }


}
