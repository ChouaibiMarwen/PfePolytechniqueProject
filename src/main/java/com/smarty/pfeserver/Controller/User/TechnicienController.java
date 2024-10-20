package com.smarty.pfeserver.Controller.User;

import com.smarty.pfeserver.Enum.User.Gender;
import com.smarty.pfeserver.Models.Auth.Privilege;
import com.smarty.pfeserver.Models.Tools.Address;
import com.smarty.pfeserver.Models.Tools.PersonalInformation;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Models.country.Root;
import com.smarty.pfeserver.Models.country.State;
import com.smarty.pfeserver.Request.User.TechnicienRequest;
import com.smarty.pfeserver.Services.Country.CountriesServices;
import com.smarty.pfeserver.Services.Tools.AddressServices;
import com.smarty.pfeserver.Services.Tools.PersonalInformationService;
import com.smarty.pfeserver.Services.User.RoleService;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Services.auth.PrivilegeService;
import com.smarty.pfeserver.Services.criteria.CriteriaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value="api/v1/technician")
public class TechnicienController {

    @Autowired
    private UserService userService;
    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CountriesServices countriesServices;
    @Autowired
    private AddressServices addressServices;
    @Autowired
    private CriteriaService criteriaService;

    @Autowired
    private PrivilegeService privilegeService;
    private static final List<String> adminPrivileges = Arrays.asList("USER_READ", "USER_WRITE");

    @PostMapping(value = {"/add_technicien"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "add technicien for admin", notes = "Endpoint to add technicien")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data phone_number or email or first-name-ar or first-name-en or last-name-en or last-name-ar is null"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 409, message = "Conflict, phone-number or email or user-name is already exists"),
            @ApiResponse(code = 406, message = "Not Acceptable , the email is not valid")
    })
    public ResponseEntity<users> add_supplier(@RequestBody TechnicienRequest request) throws IOException, InterruptedException, MessagingException {
        if (request.getEmail() == null)
            return new ResponseEntity("email", HttpStatus.BAD_REQUEST);
        if (request.getPhonenumber() == null)
            return new ResponseEntity("phone-number", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getFirstnameen() == null)
            return new ResponseEntity("first-name-en", HttpStatus.BAD_REQUEST);
        if (request.getInformationRequest().getLastnameen() == null)
            return new ResponseEntity("last-name-en", HttpStatus.BAD_REQUEST);
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

        // Set user details
        PersonalInformation resultinformation = this.personalInformationService.save(information);
        user.setUsername(username);
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhonenumber(request.getPhonenumber().toLowerCase());
        user.setPassword(request.getPassword());
        user.setPersonalinformation(resultinformation);
        List<Privilege> privilegeList = this.privilegeService.findAll();
        Set<Privilege> privileges = user.getPrivileges();
        for (Privilege privilege : privilegeList) {
            if(adminPrivileges.contains(privilege.getName()))
                continue;
            user.getPrivileges().add(privilege);
        }
        users result = userService.saveTechnicien(user);
        Address address = new Address();

        if(request.getUseraddressRequest() != null ){
            if (request.getUseraddressRequest().getAddressline1() != null)
                address.setAddressline1(request.getUseraddressRequest().getAddressline1());
            if (request.getUseraddressRequest().getAddressline2() != null)
                address.setAddressline2(request.getUseraddressRequest().getAddressline2());
            if (request.getUseraddressRequest().getCountryName() != null) {
                Root root = this.countriesServices.countrybyname(request.getUseraddressRequest().getCountryName());
                if(root != null)
                    address.setCountry(root);
            }
            if (request.getUseraddressRequest().getPostcode() != null)
                address.setPostcode(request.getUseraddressRequest().getPostcode());
            if (request.getUseraddressRequest().getCityName() != null) {
                State state = this.countriesServices.Statebyname(request.getUseraddressRequest().getCityName());
                address.setCity(state);
            }
            if (request.getUseraddressRequest().getStreetname() != null)
                address.setStreetname(request.getUseraddressRequest().getStreetname());
        }
        address.setUser(user);
        Address addressresult = this.addressServices.save(address);
        users technicien = userService.UpdateUser(user);


        return new ResponseEntity<>(technicien, HttpStatus.OK);

    }

}
