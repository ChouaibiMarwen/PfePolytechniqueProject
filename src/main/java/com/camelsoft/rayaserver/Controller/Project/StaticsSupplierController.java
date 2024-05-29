package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.StatisticResponse;
import com.camelsoft.rayaserver.Response.Project.SupplierMainStatistic;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
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
@RequestMapping(value = "/api/v1/stats")
public class StaticsSupplierController extends BaseController {

    @Autowired
    private UserService UserServices;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private InvoiceService invoiceService;

   @GetMapping(value = {"/all_statistics_supplier"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all statistics for supplier main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<SupplierMainStatistic> all_statistics_supplier() throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
       SupplierMainStatistic response = new SupplierMainStatistic();
        response.setTotalcars(this.vehiclesService.FindAllSupplier(user.getSupplier()).size());
        response.setSoldcars(0);//need to see
        response.setLoandone(0);//need to see
        response.setSalesrevenue(0D);//need to see
        response.setPendingpayment(0D);//need to see
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/revenue-by-month")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all statistics for supplier main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Map<String, Double>>> getRevenueByMonth(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());

        List<Map<String, Double>> revenueByMonth = this.invoiceService.calculateRevenueByMonthAndSupplier(startDate, endDate,user);
        return ResponseEntity.ok(revenueByMonth);
    }
}
