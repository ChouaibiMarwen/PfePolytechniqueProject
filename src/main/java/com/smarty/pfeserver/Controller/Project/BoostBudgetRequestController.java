package com.smarty.pfeserver.Controller.Project;

import com.google.api.Http;
import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Project.Transaction;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Request.Projet.BudgetRequest;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Services.Project.BoostBudgetRequestService;
import com.smarty.pfeserver.Services.Project.MissionService;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/request")
public class BoostBudgetRequestController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private BoostBudgetRequestService boostBudgetRequestService;


    @PostMapping(value = {"/add_request/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "add boost budget request for team lead", notes = "Endpoint to add boost budget request for team lead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<BoostBudgetRequest> add_request(@PathVariable Long mission_id, @ModelAttribute BudgetRequest request) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());

        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);

        if (mission.getTeamLead() != currentuser)
            return new ResponseEntity("only mission's team lead can make request", HttpStatus.FORBIDDEN);
        if (mission.getStatus() == MissionStatusEnum.COMPLETED)
            return new ResponseEntity("can't add request mission after it is completed", HttpStatus.NOT_ACCEPTABLE);
        if (request.getAmount() == null)
            return new ResponseEntity("amount can't be null", HttpStatus.BAD_REQUEST);
        if (request.getIdMission() == null)
            return new ResponseEntity("idmission can't be null", HttpStatus.BAD_REQUEST);
        if (request.getReason() == null)
            return new ResponseEntity("reason of request can't be null", HttpStatus.BAD_REQUEST);
        BoostBudgetRequest demand = new BoostBudgetRequest();
        demand.setAmount(request.getAmount());
        demand.setMission(mission);
        demand.setReason(request.getReason());
        mission.getRequestBoostBudgetRequest().add(demand);
        demand.setCreatedby(currentuser);

        BoostBudgetRequest result = this.boostBudgetRequestService.Save(demand);
        this.missionService.Update(mission);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @PatchMapping(value = {"/cancel_request_while_pending/{request_id}"})
    @PreAuthorize("hasRole('TECHNICIEN') ")
    @ApiOperation(value = "cancel a boost budget request for teamlead", notes = "Endpoint to cancel a boost budget request for teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<BoostBudgetRequest> cancel_request_while_pending(@PathVariable Long request_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        BoostBudgetRequest request = this.boostBudgetRequestService.FindById(request_id);
        if (request == null)
            return new ResponseEntity("request not found", HttpStatus.NOT_FOUND);

        if(request.getStatus() != TransactionEnum.PENDING)
            return new ResponseEntity("only pending transactions can be cancelled", HttpStatus.FORBIDDEN);
        request.setStatus(TransactionEnum.CANCELLED);
        BoostBudgetRequest result = this.boostBudgetRequestService.Update(request);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PatchMapping(value = {"/approve_request/{request_id}"})
    @PreAuthorize("hasRole('ADMIN') ")
    @ApiOperation(value = "approve a request for admin", notes = "Endpoint to approve a request for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<BoostBudgetRequest> approve_transaction_by_teamlead(@PathVariable Long request_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        BoostBudgetRequest request = this.boostBudgetRequestService.FindById(request_id);
        if (request == null)
            return new ResponseEntity("request not found", HttpStatus.NOT_FOUND);
        if(request.getStatus() == TransactionEnum.CANCELLED)
            return new ResponseEntity("this request is canceled by the technician that create", HttpStatus.FORBIDDEN);
        request.setStatus(TransactionEnum.APPROVED);
        Mission mission = request.getMission();
        if(mission == null)
            return new ResponseEntity("mission of that request is  not found", HttpStatus.NOT_FOUND);
        Double oldbudget = mission.getBudget();
        Double newbudget = oldbudget + request.getAmount();
        mission.setBudget(newbudget);
        this.missionService.Update(mission);
        BoostBudgetRequest result = this.boostBudgetRequestService.Update(request);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PatchMapping(value = {"/reject_request/{request_id}"})
    @PreAuthorize("hasRole('ADMIN') ")
    @ApiOperation(value = "reject a request for admin", notes = "Endpoint to approve a request for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<BoostBudgetRequest> reject_request(@PathVariable Long request_id, @RequestParam(required = false) String reason) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        BoostBudgetRequest request = this.boostBudgetRequestService.FindById(request_id);
        if (request == null)
            return new ResponseEntity("request not found", HttpStatus.NOT_FOUND);
        if(request.getStatus() == TransactionEnum.CANCELLED)
            return new ResponseEntity("this request is canceled by the technician that create", HttpStatus.FORBIDDEN);
        request.setStatus(TransactionEnum.REJECTED);
        if(reason != null){
            request.setRejectionreason(reason);
        }
        BoostBudgetRequest result = this.boostBudgetRequestService.Update(request);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }



    @GetMapping(value = {"/get_mission_requests_list/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get mission request lit for admin and teamlead", notes = "Endpoint to get mission request lit for admin and teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Set<BoostBudgetRequest>> get_mission_requests_list(@PathVariable Long mission_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        Set<BoostBudgetRequest> result = mission.getRequestBoostBudgetRequest();
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/get_mission_requests_pg/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get mission request pagination for admin and teamlead", notes = "Endpoint to get mission request lit for admin and teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> get_mission_requests_pg(@PathVariable Long mission_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        List<BoostBudgetRequest> result = new ArrayList<>(mission.getRequestBoostBudgetRequest());
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), result.size());
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<BoostBudgetRequest> listPage = new PageImpl<>(result.subList(start, end), pageable, result.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/get_request_by_id/{request_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get mission request by id for admin and teamlead", notes = "Endpoint to get mission request lit for admin and teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<BoostBudgetRequest> get_request_by_id(@PathVariable Long request_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        BoostBudgetRequest request = this.boostBudgetRequestService.FindById(request_id);
        if (request == null)
            return new ResponseEntity("request not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping(value = {"/get_all_my_requests_pg"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get mission request pagination for current user", notes = "Endpoint to get mission request lit for currentuser teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> get_my_requests_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        List<BoostBudgetRequest> result = this.boostBudgetRequestService.findUserRequests(currentuser);
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), result.size());
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<BoostBudgetRequest> listPage = new PageImpl<>(result.subList(start, end), pageable, result.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/get_all_requests_pg"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get mission request pagination for admin", notes = "Endpoint to get all request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> get_all_requests_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        DynamicResponse dynamicresponse = this.boostBudgetRequestService.FindAllPg(page, size);
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);
    }
}
