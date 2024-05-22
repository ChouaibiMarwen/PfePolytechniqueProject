package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.AgentStatsResponse;
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
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/agent_stats")
public class AgnetStats extends BaseController {
    @Autowired
    private UserService UserServices;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(value = {"/all_agent_stats"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all statistics for admin main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<AgentStatsResponse> all_statistics_admin() throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        AgentStatsResponse response = new AgentStatsResponse();
        response.setTotalAgent(this.UserServices.totalSubAdmin());
        response.setPaidnvoice(this.invoiceService.countPaidInvoicesCreatedBySubAdmin(InvoiceStatus.PAID));
        response.setUnpaidinvoice(this.invoiceService.countPaidInvoicesCreatedBySubAdmin(InvoiceStatus.UNPAID));
        response.setTotallinvoice(this.invoiceService.countInvoicesCreatedBySubAdmins());
        response.setTotallinvoiceinprogress( invoiceService.countInvoicesCreatedBySubAdminsAndNotPaid());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping("/paied_invoice-by-month")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get paid invoices by month admin main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Map<String, Double>>> agentsPaidInvoicesByMonth(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<Map<String, Double>> revenueByMonth = this.invoiceService.calculateRevenueByMonthForSubAdmins(startDate, endDate);
        return ResponseEntity.ok(revenueByMonth);
    }
}
