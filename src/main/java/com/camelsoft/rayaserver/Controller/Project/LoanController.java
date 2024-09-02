package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanType;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.AvailiabilityEnum;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.DTO.LoanDto;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.LoanRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.LoansResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/loans")
public class LoanController extends BaseController {
    private final Log logger = LogFactory.getLog(LoanController.class);
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private LoanServices Services;
    @Autowired
    private UserService UserServices;
    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private LoanServices loanServices;

    @GetMapping(value = {"/all_loans_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all loan by status for admin", notes = "Endpoint to get loan request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_loans_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) List<LoanStatus> status,  @RequestParam(required = false) Date creationdate) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(this.Services.FindAllByStateAndDatenNewerThen(page, size, status, creationdate), HttpStatus.OK);
    }
    @GetMapping(value = {"/total_loans_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get total and amount loans for admin", notes = "Endpoint to get total and amount loans")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<LoansResponse> total_loans_admin() throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);

        LoansResponse response = new LoansResponse();
        response.setTotalloanamounts(this.Services.totalLoansAmounts());
        response.setTotalloans(this.Services.totalNotArchiveLoans());


        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/my_total_loans_amount"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER')  or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get total and amount loans for supplier", notes = "Endpoint to get total and amount loans")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Double> total_loans_supplier() throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if(user.getSupplier() == null)
            return new ResponseEntity("you are not a supplier", HttpStatus.FORBIDDEN);
        LoansResponse response = new LoansResponse();
        Double allloanscount = loanServices.totalNotArchiveLoansForSupplier(user.getSupplier());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(allloanscount, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_loans_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all loan by status for supplier", notes = "Endpoint to get loan request for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<DynamicResponse> all_loans_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) LoanStatus status) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        DynamicResponse result = new DynamicResponse();
        if (status != null) {
            result = this.Services.FindAllByStateAndSupplier(page, size, status, user.getSupplier());
        } else {
            result = this.Services.FindAllBySupplier(page, size, user.getSupplier());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add_loan")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Add a new loan request from the supplier", notes = "Endpoint to add a new loan request for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<LoanDto> addLoan(
            @ModelAttribute LoanRequest request,
            @RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        if(supplier == null){
            return new ResponseEntity("supplier not founded",HttpStatus.NOT_ACCEPTABLE);
        }
        // Processing attachment if provided
        MediaModel resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {
            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia = filesStorageService.save_file_local(attachment, "Loans");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        // Creating Loan object
        Loan loan = new Loan(
                request.getEnglishfirstname(),
                request.getEnglishlastname(),
                request.getPhonenumber(),
                request.getLoantype(),
                request.getLoanamount(),
                resourceMedia,
                supplier,
                request.getCurrency()
        );
        loan.setStatus(LoanStatus.WAITING);
        if(request.getVehicleid() != null) {
            Vehicles vehicle = this.vehiclesService.FindById(request.getVehicleid());
            if (vehicle == null)
                return new ResponseEntity("vehicle not found by this id: " + request.getVehicleid(), HttpStatus.NOT_FOUND);
            if(vehicle.getAvailiability() != AvailiabilityEnum.INSTOCK)
                return new ResponseEntity("vehicle not available", HttpStatus.NOT_FOUND);
            loan.setVehicle(vehicle);
        }
        // Saving the loan object
        Loan result = this.Services.Save(loan);
        LoanDto resultdto = new LoanDto().mapLoanToDto(result);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(resultdto, HttpStatus.OK);
    }

    @DeleteMapping("/remove_loan/{id}")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Remove loan request from the supplier", notes = "Endpoint to remove a loan request for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed the loan request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<String> removeLoan(@PathVariable Long id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        if (this.Services.ExistByIdAndSupplier(id, supplier)) {
            this.Services.DeleteById(id);
            //save new action
            UserAction action = new UserAction(
                    UserActionsEnum.LOAN_MANAGEMENT,
                    user
            );
            this.userActionService.Save(action);
            return ResponseEntity.ok().body("Loan request removed successfully");
        } else {
            return ResponseEntity.badRequest().body("This loan request does not exist");
        }

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "Remove loan request from the supplier", notes = "Endpoint to remove a loan request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed the loan request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 406, message = "Not Acceptable,this request is not related to supplier"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<LoanDto> get_loan_by_id(@PathVariable Long id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if(!this.Services.ExistById(id))
            return new ResponseEntity("this id is not found in the system", HttpStatus.BAD_REQUEST);
        Loan result = this.Services.FindById(id);
        LoanDto resultdto = new LoanDto().mapLoanToDto(result);


        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        if(user.getSupplier()!=null){
            Supplier supplier = user.getSupplier();
            if (this.Services.ExistByIdAndSupplier(id, supplier)) {

                return new ResponseEntity<>(resultdto, HttpStatus.OK);
            } else {
                return new ResponseEntity("this invoice is not related to this supplier", HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>(resultdto, HttpStatus.OK);

        }

    }

    @PatchMapping(value = {"/approve_loan/{loan_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "approve loan for admin ", notes = "Endpoint approve loan for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<LoanDto> approve_loan(@PathVariable Long loan_id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (this.Services.FindById(loan_id)== null) {
            return new ResponseEntity(loan_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Loan loan = this.Services.FindById(loan_id);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setProcessedby(user);
        Loan result = this.Services.Update(loan);
        LoanDto resultdto = new LoanDto().mapLoanToDto(result);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(resultdto, HttpStatus.OK);


    }

    @PatchMapping(value = {"/reject_loan/{loan_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "reject loan for admin ", notes = "Endpoint reject loan for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<LoanDto> reject_loan(@PathVariable Long loan_id, @RequestParam(required = false) String rejectraison) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (this.Services.FindById(loan_id)== null) {
            return new ResponseEntity(loan_id + " is not found in the system!", HttpStatus.NOT_FOUND);
        }
        Loan loan = this.Services.FindById(loan_id);
        loan.setStatus(LoanStatus.REJECTED);
        loan.setProcessedby(user);
        if(rejectraison != null)
            loan.setRejectraison(rejectraison);
        Loan result = this.Services.Update(loan);
        LoanDto resultdto = new LoanDto().mapLoanToDto(result);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.LOAN_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(resultdto, HttpStatus.OK);

    }

}
