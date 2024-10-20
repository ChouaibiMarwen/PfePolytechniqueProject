package com.smarty.pfeserver.Controller.Project;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Response.Project.MissionByStatusStats;
import com.smarty.pfeserver.Response.Project.TransactionByStatus;
import com.smarty.pfeserver.Services.Project.BoostBudgetRequestService;
import com.smarty.pfeserver.Services.Project.MissionService;
import com.smarty.pfeserver.Services.Project.TransactionService;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/stats")
public class AdminStatsController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BoostBudgetRequestService boostBudgetRequestService;

    @GetMapping(value = {"/total_technicians_count"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_technicians_count for admin", notes = "Endpoint to get total_technicians_count for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Long> total_technicians_count() throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Long total = this.userService.countusersbyRole(RoleEnum.ROLE_TECHNICIEN);
        return new ResponseEntity<>(total, HttpStatus.OK);

    }

    @GetMapping(value = {"/total_transactions_count"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_transactions_count for admin", notes = "Endpoint to get total_transactions_count for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Long> total_transactions_count() throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Long total = this.transactionService.Count();
        return new ResponseEntity<>(total, HttpStatus.OK);

    }

    @GetMapping(value = {"/total_requestes_count"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_requestes_count for admin", notes = "Endpoint to get total_requestes_count for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Long> total_requestes_count() throws IOException {
        Long total =  this.boostBudgetRequestService.Count();
        return new ResponseEntity<>(total, HttpStatus.OK);

    }

    @GetMapping(value = {"/total_mission_count"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_mission_count for admin", notes = "Endpoint to get total_mission_count for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Long> total_mission_count() throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
       Long result = this.missionService.Count();
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/total_mission_budget"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_mission_budget for admin", notes = "Endpoint to get total_mission_budget for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Double> total_mission_budget() throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Double result = this.missionService.totalmessionsBudgetcount();
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/total_missions_by_status"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_missions_by_status for admin", notes = "Endpoint to get total_missions_by_status for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<MissionByStatusStats>> total_missions_by_status() throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        List<MissionByStatusStats> list = new ArrayList<>();
        Integer totalpending = this.missionService.countallByStatus(MissionStatusEnum.PENDING);
        MissionByStatusStats pending = new MissionByStatusStats(MissionStatusEnum.PENDING, totalpending);
        list.add(pending);

        Integer totalcomp = this.missionService.countallByStatus(MissionStatusEnum.COMPLETED);
        MissionByStatusStats compl = new MissionByStatusStats(MissionStatusEnum.COMPLETED, totalcomp);
        list.add(compl);


        Integer totalcanceled = this.missionService.countallByStatus(MissionStatusEnum.CANCELLED);
        MissionByStatusStats canceled = new MissionByStatusStats(MissionStatusEnum.CANCELLED, totalcanceled);
        list.add(canceled);

        Integer totalOVERDUE = this.missionService.countallByStatus(MissionStatusEnum.OVERDUE);
        MissionByStatusStats OVERDUE = new MissionByStatusStats(MissionStatusEnum.OVERDUE, totalOVERDUE);
        list.add(OVERDUE);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping(value = {"/total_transactions_by_status"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_transactions_by_status for admin", notes = "Endpoint to get total_transactions_by_status for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<TransactionByStatus>> total_transactions_by_status() throws IOException {
        List<TransactionByStatus> list = new ArrayList<>();
        Integer totalpending = this.transactionService.CountByStatus(TransactionEnum.PENDING);
        TransactionByStatus pending = new TransactionByStatus(TransactionEnum.PENDING, totalpending);
        list.add(pending);

        Integer totalapprov = this.transactionService.CountByStatus(TransactionEnum.APPROVED);
        TransactionByStatus compl = new TransactionByStatus(TransactionEnum.APPROVED, totalapprov);
        list.add(compl);


        Integer totalreject = this.transactionService.CountByStatus(TransactionEnum.REJECTED);
        TransactionByStatus canceled = new TransactionByStatus(TransactionEnum.REJECTED, totalreject);
        list.add(canceled);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = {"/total_requests_by_status"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get total_requests_by_status for admin", notes = "Endpoint to get total_requests_by_status for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<TransactionByStatus>> total_requests_by_status() throws IOException {
        List<TransactionByStatus> list = new ArrayList<>();
        Integer totalpending = this.boostBudgetRequestService.CountByStatus(TransactionEnum.PENDING);
        TransactionByStatus pending = new TransactionByStatus(TransactionEnum.PENDING, totalpending);
        list.add(pending);

        Integer totalapprov = this.boostBudgetRequestService.CountByStatus(TransactionEnum.APPROVED);
        TransactionByStatus compl = new TransactionByStatus(TransactionEnum.APPROVED, totalapprov);
        list.add(compl);


        Integer totalreject = this.boostBudgetRequestService.CountByStatus(TransactionEnum.REJECTED);
        TransactionByStatus canceled = new TransactionByStatus(TransactionEnum.REJECTED, totalreject);
        list.add(canceled);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
