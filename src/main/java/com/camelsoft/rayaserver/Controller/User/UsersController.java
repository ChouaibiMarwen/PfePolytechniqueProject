package com.camelsoft.rayaserver.Controller.User;



import com.camelsoft.rayaserver.Enum.User.*;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
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

import com.camelsoft.rayaserver.Services.Tools.AddressServices;
import com.camelsoft.rayaserver.Services.Tools.BankAccountService;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.*;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
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
    private BankAccountService bankAccountService;
    @Autowired
    private AddressServices addressServices;

    @Autowired
    private CriteriaService criteriaService;

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<users> GetCurrentUser() throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_current_user_personal_information"})
    @PreAuthorize("hasRole('ADMIN')")
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

        PersonalInformation result = this.personalInformationService.update(personalInformation);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @PatchMapping(value = {"/update_user_personal_information/{userId}"})
    @PreAuthorize("hasRole('ADMIN')")
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

        }
        PersonalInformation result = this.personalInformationService.update(information);
        String name = result.getFirstnameen() + result.getLastnameen();
        String username = userService.GenerateUserName(name, userService.Count());
        user.setUsername(username);
        this.userService.UpdateUser(user);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/logout")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<users> get_user_by_id(@PathVariable Long userid) throws IOException {
        if (!this.userService.existbyid(userid))
            return new ResponseEntity("user not exist", HttpStatus.NOT_FOUND);
        users result = this.userService.findById(userid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/users_list"})
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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


    @PostMapping(value = {"/add_Bank_account/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
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
        BankInformation b = this.userService.addBankAccounToUser(user, request);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

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
        if (bankInformation != null) {
            return ResponseEntity.ok(bankInformation);
        } else {
            return new ResponseEntity("Failed to update billing address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"bank_information/{bankInformationId}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<BankInformation> getBankInformation(@PathVariable Long bankInformationId) throws IOException {
        BankInformation bankInformation = this.bankAccountService.findById(bankInformationId);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

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
    public ResponseEntity<users> updateUserActivation(@PathVariable Long id) {
        users user = this.userService.updateActivatedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value = {"address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request, invalid ID format or missing Id"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden, access denied. Requires admin role"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<Address> getAddressUser(@PathVariable Long id) throws IOException {
        Address result = this.addressServices.findById(id);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return new ResponseEntity("Failed to fetch address", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @DeleteMapping(value = {"/{user_id}"})
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) throws IOException {
        Address Address = this.addressServices.findById(addressId);
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action

        this.addressServices.deleteAddress(Address);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }


}
