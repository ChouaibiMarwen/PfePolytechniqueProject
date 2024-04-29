package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Tools.BankInformationRequest;
import com.camelsoft.rayaserver.Request.Tools.BillingAddressRequest;
import com.camelsoft.rayaserver.Request.auth.SupplierSingUpRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
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

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/suppliers")
public class SupplierController {
    @Autowired
    private UserService userService;
    @Autowired
    private PersonalInformationService personalInformationService;
    @Autowired
    private SupplierServices supplierServices;

    @PostMapping(value = {"/add"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add suppliers for admin", notes = "Endpoint to add suppliers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 409, message = "Conflict, phone-number or email or user-name is already exists"),
            @ApiResponse(code = 406, message = "Not Acceptable , the email is not valid")
    })
    public ResponseEntity<users> add_supplier(@RequestBody SupplierSingUpRequest request) throws IOException, InterruptedException, MessagingException {
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

        String phonenumber = request.getPhonenumber().replaceAll("[\\s()]", "");
        if (userService.existbyphonenumber(phonenumber))
            return new ResponseEntity("phone-number", HttpStatus.CONFLICT);
        if (userService.existbyemail(request.getEmail().toLowerCase()))
            return new ResponseEntity("email", HttpStatus.CONFLICT);
        // Check email format
        if (!UserService.isValidEmail(request.getEmail().toLowerCase()) && !request.getEmail().contains(" "))
            return new ResponseEntity("email", HttpStatus.NOT_ACCEPTABLE);

        String name = request.getInformationRequest().getFirstnameen() + request.getInformationRequest().getLastnameen();
        String username = userService.GenerateUserName(name, userService.Count());
        users existingUserByUsername = userService.findByUserName(username);
        if (existingUserByUsername != null)
            return new ResponseEntity("user-name", HttpStatus.CONFLICT);
        // Create a new user
        users user = new users();
        PersonalInformation information = new PersonalInformation();
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
            information.setGender(request.getInformationRequest().getGender());
        if (request.getInformationRequest().getWorksector() != null)
            information.setWorksector(request.getInformationRequest().getWorksector());
        if (request.getInformationRequest().getMaritalstatus() != null)
            information.setMaritalstatus(request.getInformationRequest().getMaritalstatus());
        // Set user details
        PersonalInformation resultinformation = this.personalInformationService.save(information);
        Supplier supplier = new Supplier();
        supplier.setSuppliernumber(request.getSuppliernumber());
        Supplier resultsupplier = this.supplierServices.save(supplier);
        user.setUsername(username);
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(request.getPassword());
        user.setPersonalinformation(resultinformation);
        user.setSupplier(resultsupplier);
        // Save the user
        users result = userService.saveSupplier(user);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/all"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all supplier by status for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<DynamicResponse> all(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) Boolean active, @RequestParam(required = false) String name) throws IOException {
        return new ResponseEntity<>(this.userService.filterAllUser(page, size, active, name, RoleEnum.ROLE_SUPPLIER), HttpStatus.OK);
    }


  @PatchMapping(value = {"/verified/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update supplier verified to the opposit", notes = "Endpoint to update supplier's verified attribute")
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully add"),
    @ApiResponse(code = 400, message = "Bad request, check the id supplier "),
    @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
    @ApiResponse(code = 404, message = "Supllier not found with that id")
    })
    public ResponseEntity<users> updateUserVerification(@PathVariable Long id){
        users  user =  this.userService.updateVerifiedUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = {"/add_Billing_Address/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "add Billing address", notes = "Endpoint to add billing address to a supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addSupplierBillingAddress(@PathVariable Long id,  @RequestBody BillingAddressRequest request) throws IOException, InterruptedException, MessagingException {
       /* if (request.getEmail() == null)
            return new ResponseEntity("email", HttpStatus.BAD_REQUEST);
        .
        .
        .
*/
        users user = this.userService.findById(id);
        if (user == null) {
            return new ResponseEntity("Can't find user by that id", HttpStatus.CONFLICT);
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
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 406, message = "Not Acceptable , the id is not valid")
    })
    public ResponseEntity<users> addSupplierBankAccount(@PathVariable Long id,  @RequestBody BankInformationRequest request) throws IOException, InterruptedException, MessagingException {
       /* if (request.getEmail() == null)
            return new ResponseEntity("email", HttpStatus.BAD_REQUEST);
        .
        .
        .
*/
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



}
