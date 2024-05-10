package com.camelsoft.rayaserver.Controller.Project.AdminStatistics;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.Project.ProductService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/statistics")
public class AdminOverviewStatictics {


    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private ProductService productService;


    @GetMapping(value = {"/total_suppliers_count"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "total suppliers count for admin ", notes = "Endpoint get total suppliers count for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Long> all_suppliers_count() throws IOException {
        Long participation = this.supplierServices.countSuppliers();
        return new ResponseEntity<>( participation , HttpStatus.OK);
    }



    @GetMapping("/loan_amount_count_by_status")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "total loans count by status for admin ", notes = "Endpoint get total loans count by status for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Long> countLoansByStatus(@RequestParam LoanStatus status) {
        long count = loanServices.countByStatus(status);
        return new ResponseEntity<>( count , HttpStatus.OK);
    }


    @GetMapping("/sum_done_Loans-Amount")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "total loan amount for done loans for admin ", notes = "Endpoint get total done loans amount loans for admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Double> sumLoanAmountForDoneLoans() {
        double sum = this.loanServices.sumLoanAmountForDoneLoans();
        return new ResponseEntity<>( sum , HttpStatus.OK);
    }


    @GetMapping("/sum_Loans-Amount_by_status")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "total loan amount by status for admin ", notes = "Endpoint get total loans amount by status for  admin ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin"),
            @ApiResponse(code = 404, message = "Not found, check invoice id")
    })
    public ResponseEntity<Double> sumLoanAmountByStatus(@RequestParam LoanStatus status) {
        double sum = this.loanServices.sumLoanAmountByStatus(status);
        return new ResponseEntity<>( sum , HttpStatus.OK);
    }
}

