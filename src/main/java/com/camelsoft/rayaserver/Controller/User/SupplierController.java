package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Tools.BankInformationRequest;
import com.camelsoft.rayaserver.Request.Tools.BillingAddressRequest;
import com.camelsoft.rayaserver.Request.auth.SupplierSingUpRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.RoleService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Exception.ResourceNotFoundException;
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
@RequestMapping(value = "/api/v1/suppliers")
public class SupplierController {
    @Autowired
    private UserService userService;
    @Autowired
    private PersonalInformationService personalInformationService;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private RoleService roleService;

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
        information.setPhonenumber(phonenumber);
        information.setFirstnameen(request.getInformationRequest().getFirstnameen());
        information.setLastnameen(request.getInformationRequest().getLastnameen());
        information.setFirstnamear(request.getInformationRequest().getLastnamear());
        information.setLastnamear(request.getInformationRequest().getLastnamear());
        information.setPhonenumber(phonenumber);
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
    @ApiOperation(value = "get all suppliers without pagination", notes = "Endpoint to get suppliers")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully get"),
    })
    public ResponseEntity<List<UserShortDto>> all(@RequestParam(required = false) Boolean active, @RequestParam(required = false) String name , @RequestParam(required = false) Boolean verified) throws IOException {
        return new ResponseEntity<>(this.supplierServices.getAllUsersWithoutPagination(active, name, RoleEnum.ROLE_SUPPLIER, verified), HttpStatus.OK);
    }



}
