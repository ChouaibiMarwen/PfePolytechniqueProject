package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.StatisticResponse;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/statistics")
public class StaticsMainController   extends BaseController {
    @Autowired
    private UserService UserServices;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(value = {"/all_statistics_admin"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all statistics for admin main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<StatisticResponse> all_statistics_admin() throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        StatisticResponse response = new StatisticResponse();
        response.setTotalsupplier(this.UserServices.totalSupplier());
        response.setTotalusers(this.UserServices.totalUsers());
        response.setTotalrevenue(this.invoiceService.getTotalRevenueFromPaidInvoices());
        response.setTotalloanissued(this.loanServices.Count());
        response.setTotalloaninprogress( loanServices.countByStatus(LoanStatus.IN_PROGRESS));
        response.setTotalloandone(loanServices.countByStatus(LoanStatus.DONE));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    //add sales by month api for the admin

    @GetMapping("/total")
    public ResponseEntity<Map<String, Double>> getTotalRevenueByMonth(
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate) {

        Map<String, Double> totalRevenueByMonth = this.invoiceService.getTotalRevenueByMonth(startDate, endDate);
        return ResponseEntity.ok(totalRevenueByMonth);
    }

}
