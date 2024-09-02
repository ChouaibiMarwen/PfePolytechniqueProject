package com.camelsoft.rayaserver.Controller.User;

import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.State;
import com.camelsoft.rayaserver.Request.auth.SupplierSingUpRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Country.CountriesServices;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.SupplierClassificationService;
import com.camelsoft.rayaserver.Services.Tools.AddressServices;
import com.camelsoft.rayaserver.Services.Tools.BillingAddressService;
import com.camelsoft.rayaserver.Services.Tools.PersonalInformationService;
import com.camelsoft.rayaserver.Services.User.RoleService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/suppliers")
public class SupplierController extends BaseController {
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierServices suppliersServices;
    @Autowired
    private PersonalInformationService personalInformationService;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BillingAddressService billingAddressService;
    @Autowired
    private CountriesServices countriesServices;
    @Autowired
    private AddressServices addressServices;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    private SupplierClassificationService classificationService;

    @Autowired
    private PrivilegeService privilegeService;
    private static final List<String> adminPrivileges = Arrays.asList("USER_READ", "SUPPLIER_READ", "USER_WRITE", "SUPPLIER_WRITE", "SUB_ADMIN_READ", "SUB_ADMIN_WRITE", "CUSTOMER_READ", "CUSTOMER_WRITE", "AGENT_READ", "AGENT_WRITE", "EVENT_WRITE");

    @PostMapping(value = {"/add"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
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
        Supplier supp = this.supplierServices.findBySuppliernumber(request.getSuppliernumber());
        if(supp!=null){
            return new ResponseEntity("this supplier number is already exist : "+request.getSuppliernumber(), HttpStatus.BAD_REQUEST);
        }
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
        if (request.getVatnumber() == null)
            return new ResponseEntity("vat number is required", HttpStatus.BAD_REQUEST);

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
        supplier.setName(request.getCompanyName());
        supplier.setIdtype(request.getIdType());
        supplier.setIdnumber(request.getIdnumber());
        Supplier resultsupplier = this.supplierServices.save(supplier);
        user.setUsername(username);
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhonenumber(request.getPhonenumber().toLowerCase());
        user.setPassword(request.getPassword());
        user.setVatnumber(request.getVatnumber());
        user.setPersonalinformation(resultinformation);
        user.setSupplier(resultsupplier);
        List<Privilege> privilegeList = this.privilegeService.findAll();
        Set<Privilege> privileges = user.getPrivileges();
        for (Privilege privilege : privilegeList) {
            if(adminPrivileges.contains(privilege.getName()))
                continue;
            user.getPrivileges().add(privilege);
        }

        // add classification to supplier if founded idsupplierclassification
        if(request.getIdsupplierclassification() != null){
            SuppliersClassification classification = this.classificationService.FindById(request.getIdsupplierclassification());
            if(classification == null)
                return new ResponseEntity("classification not found with id: " + request.getIdsupplierclassification(), HttpStatus.NOT_FOUND);
            user.setSupplierclassification(classification);
        }

        users result = userService.saveSupplier(user);
        BillingAddress billingAddress = new BillingAddress();
        if(request.getBillingaddressRequest()!=null){
            if (request.getBillingaddressRequest().getAddressline1() != null)
                billingAddress.setBillingaddress(request.getBillingaddressRequest().getAddressline1());
            if (request.getBillingaddressRequest().getAddressline2() != null)
                billingAddress.setBillingaddress02(request.getBillingaddressRequest().getAddressline2());
           /* if (request.getBillingaddressRequest().getCountryName() != null)
                billingAddress.setCountry(request.getBillingaddressRequest().getCountryName());*/
            if (request.getBillingaddressRequest().getPostcode() != null)
                billingAddress.setZipcode(request.getBillingaddressRequest().getPostcode());
            if (request.getBillingaddressRequest().getCityName() != null)
                billingAddress.setCity(request.getBillingaddressRequest().getCityName());
            if (request.getBillingaddressRequest().getStreetname() != null)
                billingAddress.setStreetname(request.getBillingaddressRequest().getStreetname());
        }

        if (user.getPersonalinformation().getPhonenumber() != null)
            billingAddress.setPhonenumber(user.getPersonalinformation().getPhonenumber());
        if (user.getPersonalinformation().getFirstnameen() != null)
            billingAddress.setFirstname(user.getPersonalinformation().getFirstnameen());
        if (user.getPersonalinformation().getLastnameen() != null)
            billingAddress.setLastname(user.getPersonalinformation().getLastnameen());
        if (user.getEmail() != null)
            billingAddress.setEmail(user.getEmail());
        BillingAddress billingaddressresult = this.billingAddressService.saveBiLLAddress(billingAddress);
        user.setBillingAddress(billingaddressresult);
        users billinguserresult = userService.UpdateUser(user);

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

        /*if (request.getUseraddressRequest().getAddressline1() != null)
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
            address.setStreetname(request.getUseraddressRequest().getStreetname());*/
        address.setUser(user);
        Address addressresult = this.addressServices.save(address);
        /*user.getAddresses().add(address);
        userService.UpdateUser(user);*/



        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

        userService.UpdateUser(user);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(billinguserresult, HttpStatus.OK);

    }


    @GetMapping(value = {"/all"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')  or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all suppliers without pagination", notes = "Endpoint to get suppliers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request,"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 409, message = "Conflict, phone-number or email or user-name is already exists"),
            @ApiResponse(code = 406, message = "Not Acceptable , the email is not valid")
    })
    public ResponseEntity<List<UserShortDto>> all(@RequestParam(required = false) Boolean active, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean verified) throws IOException {
        return new ResponseEntity<>(this.supplierServices.getAllUsersWithoutPagination(active, name, RoleEnum.ROLE_SUPPLIER, verified), HttpStatus.OK);
    }

    @GetMapping(value = {"/all_my_sub_supplier_short"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all suppliers without pagination", notes = "Endpoint to get suppliers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request,"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 409, message = "Conflict, phone-number or email or user-name is already exists"),
            @ApiResponse(code = 406, message = "Not Acceptable , the email is not valid")
    })
    public ResponseEntity<List<UserShortDto>> all_my_sub_supplier_short(@RequestParam(required = false) Boolean active, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean verified) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        List<UserShortDto> result = new ArrayList<>() ;
        if(currentuser.getManager() == null && currentuser.getRole().getRole()==RoleEnum.ROLE_SUPPLIER){
            result =  this.supplierServices.getAllSubSupplierWithoutPaginationSupplier(active, name, RoleEnum.ROLE_SUB_SUPPLIER, verified,currentuser);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        return new ResponseEntity("this is user is not a manager", HttpStatus.BAD_REQUEST);

    }
    @GetMapping(value = {"/all_my_sub_supplier_paginated"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all suppliers without pagination", notes = "Endpoint to get suppliers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request,"),
            @ApiResponse(code = 403, message = "Forbidden, you are not an admin"),
            @ApiResponse(code = 409, message = "Conflict, phone-number or email or user-name is already exists"),
            @ApiResponse(code = 406, message = "Not Acceptable , the email is not valid")
    })
    public ResponseEntity<DynamicResponse> all_my_sub_supplier_paginated(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) Boolean active, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean verified) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());

        if(currentuser.getManager() == null && currentuser.getRole().getRole()==RoleEnum.ROLE_SUPPLIER){
            PageImpl<users>  result =  this.criteriaService.UsersSearchCreatiriaRolesListSubsupplier(page,size,active, name, RoleEnum.ROLE_SUB_SUPPLIER, verified,currentuser,false);
            DynamicResponse respense = new DynamicResponse(result.getContent(), result.getNumber(), result.getTotalElements(), result.getTotalPages());
            return new ResponseEntity<>(respense, HttpStatus.OK);

        }
        return new ResponseEntity("this is user is not a manager", HttpStatus.BAD_REQUEST);

    }
    @GetMapping(value = {"/all_suppliers_by_purchase_order_status"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all suppliers by purchases order status for admin", notes = "Endpoint to get all suppliers by purchases order status for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_suppliers_by_purchase_order_status(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) PurshaseOrderStatus status, @RequestParam(required = false) String name) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(this.suppliersServices.DynamicResponsefindyNameorPurshaseOrderStatus(page, size, name, status), HttpStatus.OK);

    }
    @GetMapping(value = {"/all_suppliers_with_available_vehecles_stock"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all suppliers that have available stock for admin", notes = "Endpoint get all suppliers that have available vehecles' stock for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE, you need to select related"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_suppliers_with_available_vehecles_stock(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) PurshaseOrderStatus status, @RequestParam(required = false) String name) throws IOException {
        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        if(currentuser.getRole().getRole() == RoleEnum.ROLE_SUB_ADMIN){
            SuppliersClassification classification = currentuser.getSupplierclassification();
            if(classification != null)
                return new ResponseEntity<>(this.suppliersServices.getAllSuppliersHavingAvailbalVeheclesStockForSubAdminWithClassification(page, size, classification), HttpStatus.OK);
            else
                return  new ResponseEntity("this sub-admin have not any classification yet", HttpStatus.NOT_ACCEPTABLE);
        }
        // if the curent user is admin, he will get all list without classifications
        return new ResponseEntity<>(this.suppliersServices.getAllSuppliersHavingAvailbalVeheclesStock(page, size), HttpStatus.OK);

    }


    //api to update or delete supplier classification classification
    @PatchMapping(value = {"/update_supplier_classification/{supplier_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    public ResponseEntity<users> update_supplier_classification(@PathVariable Long supplier_id ,@RequestParam(required = false) Long classification_id) throws IOException, InterruptedException {
        users user = userService.findById(supplier_id);
        if(user == null)
            return new ResponseEntity("supplier is not found", HttpStatus.NOT_FOUND);

        //old user classification if founded
        SuppliersClassification c = user.getSubadminClassification();
        if(c != null)
            c.getSuppliers().remove(user);

        if(classification_id == null){
            user.setSupplierclassification(null);


        }else{
            //new classification
            SuppliersClassification classresult = this.classificationService.FindById(classification_id);

            if (classresult == null)
                return new ResponseEntity("classification not found with id: " + classification_id, HttpStatus.NOT_FOUND);
            user.setSupplierclassification(classresult);
        }
        users  resultuser =  userService.UpdateUser(user);
        //manually set the postLoad values in the first return
        if (resultuser.getSupplierclassification() == null) {
            resultuser.getSupplier().setClassificationId(null);
            resultuser.getSupplier().setClassificationname(null);
        }else{
            resultuser.getSupplier().setClassificationId(resultuser.getSupplierclassification().getId());
            resultuser.getSupplier().setClassificationname(resultuser.getSupplierclassification().getName());
        }

        users currentuser = userService.findByUserName(getCurrentUser().getUsername());
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIERS_CLASSIFICATION_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity(resultuser, HttpStatus.OK);
    }


}
