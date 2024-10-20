package com.smarty.pfeserver.Controller.Project;

import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Services.Project.BoostBudgetRequestService;
import com.smarty.pfeserver.Services.Project.MissionService;
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
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/stats")
public class AdminStatsController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private BoostBudgetRequestService boostBudgetRequestService;

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
}
