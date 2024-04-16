package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.LoanStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.LoanRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
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
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/loans")
public class LoanController extends BaseController {
    private final Log logger = LogFactory.getLog(LoanController.class);
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");

    @Autowired
    private LoanServices Services;
    @Autowired
    private UserService UserServices;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @GetMapping(value = {"/all_loans_admin"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DynamicResponse> all_loans_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) LoanStatus status) throws IOException {
        // users user = UserServices.findByUserName(getCurrentUser().getUsername());
        DynamicResponse result = new DynamicResponse();
        if (status != null) {
            result = this.Services.FindAllByState(page, size, status);
        } else {
            result = this.Services.FindAllPg(page, size);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_loans_supplier"})
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<DynamicResponse> all_loans_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) LoanStatus status) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        DynamicResponse result = new DynamicResponse();
        if (status != null) {
            result = this.Services.FindAllByStateAndSupplier(page, size, status, user.getSupplier());
        } else {
            result = this.Services.FindAllBySupplier(page, size, user.getSupplier());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add_loan")
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "Add a new loan request from the supplier", notes = "Endpoint to add a new loan request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the loan request"),
            @ApiResponse(code = 400, message = "Bad request, the file not saved or the type is mismatch"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Loan> addLoan(
            @ModelAttribute LoanRequest request,
            @RequestParam(value = "file", required = false) MultipartFile attachment) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        // Processing attachment if provided
        File_model resourceMedia = null;
        if (attachment != null && !attachment.isEmpty()) {
            String extension = attachment.getContentType().substring(attachment.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return ResponseEntity.badRequest().body(null);
            }
            resourceMedia = filesStorageService.save_file(attachment, "Loans");
            if (resourceMedia == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        // Creating Loan object
        Loan loan = new Loan(
                request.getEnglishfirstname(),
                request.getEnglishlastname(),
                request.getEnglishsecondname(),
                request.getEnglishthirdname(),
                request.getFamilyname(),
                request.getFathername(),
                request.getGrandfathername(),
                request.getBirthdate(),
                request.getEmail(),
                request.getPhonenumber(),
                request.getPostcode(),
                request.getUnitnumber(),
                request.getName(),
                request.getRetirementdate(),
                request.getSectortype(),
                request.getCopynumber(),
                request.getAdditionalnumber(),
                request.getBuildingnumber(),
                request.getMaritalstatus(),
                request.getNumberofdependents(),
                request.getNationalid(),
                request.getNationalidissuedate(),
                request.getNationalidexpirydate(),
                request.getCity(),
                request.getDistrict(),
                request.getPrimaryaddress(),
                request.getStreetname(),
                request.getWorksector(),
                request.getSalary(),
                request.getEmployername(),
                request.getLoantype(),
                request.getFirstinstallment(),
                request.getPurposeofloan(),
                request.getBalloonloan(),
                request.getLoanamount(),
                request.getLoanterm(),
                resourceMedia,
                request.getCarmark(),
                request.getCarmodel(),
                request.getCaryear(),
                request.getCarvin(),
                request.getCarcolor(),
                request.getCarquantity(),
                request.getNote(),
                supplier
        );

        // Saving the loan object
        Loan result = this.Services.Save(loan);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/remove_loan/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "remove loan request from the supplier", notes = "Endpoint to add a new loan request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully remove the loan request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity remove_loan(@PathVariable Long id) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        if(this.Services.ExistByIdAndSupplier(id,supplier)){
            this.Services.DeleteById(id);
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.badRequest().body("this loan does not exist");
    }
}
