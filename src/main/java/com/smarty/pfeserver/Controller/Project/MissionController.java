package com.smarty.pfeserver.Controller.Project;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Project.Location;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Request.Projet.MissionRequest;
import com.smarty.pfeserver.Request.Projet.UpdateMissionRequest;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Services.Project.LocationService;
import com.smarty.pfeserver.Services.Project.MissionService;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Services.criteria.CriteriaService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/mission")
public class MissionController extends BaseController {

    @Autowired
    private MissionService missionService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private UserService userService;

    @PostMapping(value = {"/add_mission"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a mission for admin", notes = "Endpoint to Add a mission for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })

    public ResponseEntity<Mission> add_mission(@ModelAttribute MissionRequest request) throws IOException {
        /*users user = this.userService.findByUserName(getCurrentUser().getUsername());*/

        if(request.getTitle() == null)
            return new ResponseEntity("name can't be null", HttpStatus.NOT_ACCEPTABLE);
        if(request.getBudget() == null)
            return new ResponseEntity("budget can't be null", HttpStatus.NOT_ACCEPTABLE);
        if(request.getStartdate() == null)
            return new ResponseEntity("start date can't be null", HttpStatus.NOT_ACCEPTABLE);
        if(request.getEnddate() == null)
            return new ResponseEntity("end date can't be null", HttpStatus.NOT_ACCEPTABLE);
        if(request.getIdTeamLead() == null)
            return new ResponseEntity("id team lead can't be null", HttpStatus.NOT_ACCEPTABLE);
        Mission mission = new Mission();
        mission.setTitle(request.getTitle());
        mission.setBudget(request.getBudget());
        mission.setStartdate(request.getStartdate());
        mission.setAddress(request.getAddress());
        if(request.getFileurl() != null)
            mission.setFileurl(request.getFileurl());
        mission.setEnddate(request.getEnddate());
        if(request.getIdTechniciens()!= null){
            for(Long id : request.getIdTechniciens()){
                users user = this.userService.findById(id);
                if(user == null)
                    return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
                if(this.missionService.hasMissionInPeriod(request.getStartdate(), request.getEnddate(), user))
                    return new ResponseEntity("user with id : "+ id +" already have mission into this periode ", HttpStatus.NOT_ACCEPTABLE);
                mission.addParticipant(user);
            }
        }
        users teamlead = this.userService.findById(request.getIdTeamLead());
        if(teamlead == null)
            return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        if(!mission.getParticipants().contains(teamlead))
            mission.addParticipant(teamlead);
        mission.setTeamLead(teamlead);
        if(request.getLocationLatitude() != null || request.getLocationLongitude() != null || request.getLocationname() != null){
            Location location = new Location();
            if(request.getLocationname() != null)
                location.setName(request.getLocationname());
            if(request.getLocationLongitude() != null)
                location.setLongitude(request.getLocationLongitude());
            if(request.getLocationLatitude() != null)
                location.setLatitude(request.getLocationLatitude());
            mission.setLocation(location);
        }
        Mission result = this.missionService.Save(mission);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_mission_participants/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update mission participants for admin", notes = "Endpoint to update mission participants for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Object> updateMissionParticipants(@PathVariable Long mission_id, @RequestBody List<Long> idParticipants) throws IOException {
        // Fetch the mission by ID
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null) {
            return new ResponseEntity<>("Mission not found", HttpStatus.NOT_FOUND);
        }

        // Fetch the current participants
        Set<users> currentParticipants = mission.getParticipants();
        Set<users> newParticipants = new HashSet<>();

        // Iterate over the new list of participant IDs
        if (idParticipants != null && !idParticipants.isEmpty()) {
            for (Long id : idParticipants) {
                users user = this.userService.findById(id);
                if (user == null) {
                    return new ResponseEntity<>("User with ID " + id + " not found", HttpStatus.NOT_FOUND);
                }
                // Add the user to the mission if not already present
                if (!currentParticipants.contains(user)) {
                    mission.addParticipant(user);  // This will add the mission to the user's missions as well
                }
                newParticipants.add(user);  // Keep track of new participants
            }
        }

        // Remove participants that are no longer in the new list
        for (users currentUser : currentParticipants) {
            if (!newParticipants.contains(currentUser)) {
                currentUser.removeMission(mission);  // This will remove the mission from the user's missions
                this.userService.UpdateUser(currentUser);  // Save changes to the user
            }
        }

        // Update the mission in the database
        Mission result = this.missionService.Update(mission);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_mission/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update a mission for admin", notes = "Endpoint to update a mission for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Mission> all_my_notification( @PathVariable Long mission_id,@RequestParam UpdateMissionRequest request) throws IOException {
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);

        if(mission.getStatus() == MissionStatusEnum.COMPLETED)
            return new ResponseEntity("can't update mission after it is completed", HttpStatus.NOT_ACCEPTABLE);
        if(request.getTitle() != null)
            mission.setTitle(request.getTitle());
        if(request.getBudget() != null){
            if(mission.getStatus() == MissionStatusEnum.COMPLETED)
                return new ResponseEntity("can't update budget while it is completed", HttpStatus.NOT_ACCEPTABLE);
            mission.setBudget(request.getBudget());
        }
        if(request.getFileurl() != null)
            mission.setFileurl(request.getFileurl());
        if(request.getStartdate() != null){
            if(mission.getStatus() != MissionStatusEnum.PENDING)
                return new ResponseEntity("can't update start date while mission is already started or cancelled", HttpStatus.NOT_ACCEPTABLE);
            mission.setStartdate(request.getStartdate());
        }
        if(request.getEnddate() != null){
            if(mission.getStatus() == MissionStatusEnum.COMPLETED)
                return new ResponseEntity("can't update ends date while it is completed", HttpStatus.NOT_ACCEPTABLE);
            mission.setEnddate(request.getEnddate());
        }
        if(request.getStatus() != null){
            if(mission.getStatus() == MissionStatusEnum.COMPLETED)
                return new ResponseEntity("can't update misiion status while it is completed", HttpStatus.NOT_ACCEPTABLE);
            mission.setStatus(request.getStatus());
        }

        users teamlead = this.userService.findById(request.getIdTeamLead());
        if(teamlead == null)
            return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        if(!mission.getParticipants().contains(teamlead))
            mission.addParticipant(teamlead);
        mission.setTeamLead(teamlead);
        if(request.getLocationLatitude() != null || request.getLocationLongitude() != null || request.getLocationname() != null){
            Location location = mission.getLocation();
            if(request.getLocationname() != null)
                location.setName(request.getLocationname());
            if(request.getLocationLongitude() != null)
                location.setLongitude(request.getLocationLongitude());
            if(request.getLocationLatitude() != null)
                location.setLatitude(request.getLocationLatitude());
            this.locationService.Update(location);
        }
        Mission result = this.missionService.Update(mission);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_missions_pg"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all missions for admin", notes = "Endpoint to get missions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> all_missions_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title,  @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate,@RequestParam(required = false) MissionStatusEnum status,  @RequestParam(required = false) Long idTeamLead) throws IOException {
        users teamlead = null;
        if(idTeamLead != null){
            teamlead = this.userService.findById(idTeamLead);
            if(teamlead == null)
                return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        }
        List<Mission> mymissions = this.criteriaService.findMissionsWithFilters(title, startdate, enddate, teamlead,status, null);

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), mymissions.size());
        int end = Math.min((start + pageable.getPageSize()), mymissions.size());
        Page<Mission> listPage = new PageImpl<>(mymissions.subList(start, end), pageable, mymissions.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/all_missions_list"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all missions for admin", notes = "Endpoint to get missions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<Mission>> all_missions_list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title,  @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate,@RequestParam(required = false) MissionStatusEnum status,  @RequestParam(required = false) Long idTeamLead) throws IOException {
        users teamlead = null;
        if(idTeamLead != null){
            teamlead = this.userService.findById(idTeamLead);
            if(teamlead == null)
                return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        }
        List<Mission> missions = this.criteriaService.findMissionsWithFilters(title, startdate, enddate, teamlead, status, null);
        return new ResponseEntity<>(missions, HttpStatus.OK);

    }

    @PatchMapping(value = {"/update_mission_status/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "update a mission status for admin and users", notes = "Endpoint to update a mission for admin and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Mission> all_my_notification( @PathVariable Long mission_id,@RequestParam MissionStatusEnum status) throws IOException {
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        mission.setStatus(status);
        Mission result = this.missionService.Update(mission);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/mission_by_id/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get a mission sby id for admin and users", notes = "Endpoint to get a mission sby id for admin and users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Mission> mission_by_id( @PathVariable Long mission_id) throws IOException {
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null)
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(mission, HttpStatus.OK);

    }

    @GetMapping(value = {"/all_my_missions_pg"})
    @PreAuthorize("hasRole('TECHNICIEN')")
    @ApiOperation(value = "get all missions for admin", notes = "Endpoint to get missions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> all_my_missions_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title,@RequestParam(required = false) MissionStatusEnum status,  @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate,  @RequestParam(required = false) Long idTeamLead) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        users teamlead = null;
        if(idTeamLead != null){
            teamlead = this.userService.findById(idTeamLead);
            if(teamlead == null)
                return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        }
        List<Mission> mymissions = this.criteriaService.findMissionsWithFilters(title, startdate, enddate, teamlead, status,currentuser);

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), mymissions.size());
        int end = Math.min((start + pageable.getPageSize()), mymissions.size());
        Page<Mission> listPage = new PageImpl<>(mymissions.subList(start, end), pageable, mymissions.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/all_my_missions_list"})
    @PreAuthorize("hasRole('TECHNICIEN')")
    @ApiOperation(value = "get all missions for current user", notes = "Endpoint to get missions for current user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<Mission>> all_my_missions_list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title,@RequestParam(required = false) MissionStatusEnum status,  @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate,  @RequestParam(required = false) Long idTeamLead) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        users teamlead = null;
        if(idTeamLead != null){
            teamlead = this.userService.findById(idTeamLead);
            if(teamlead == null)
                return new ResponseEntity("Team lead not found", HttpStatus.NOT_FOUND);
        }
        List<Mission> missions = this.criteriaService.findMissionsWithFilters(title, startdate, enddate, teamlead, status,currentuser);

        return new ResponseEntity<>(missions, HttpStatus.OK);

    }

    @GetMapping(value = {"/all_dispo_technicians_by_periode_list"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get all dispo technicians into periode for admin and user", notes = "Endpoint to get all dispo technicians into periode for admin and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<users>> all_dispo_technicians_by_periode_list(@RequestParam Date startdate, @RequestParam Date enddate) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        List<users> resuslt = new ArrayList<>();
        List<users> list = this.userService.findAll();
        List<users> userlist = list.stream()
                .filter(u -> u.getRole().getRole() != RoleEnum.ROLE_ADMIN)
                .collect(Collectors.toList());
        for(users u : userlist){
            if(!this.missionService.hasMissionInPeriod(startdate, enddate, u)){
                resuslt.add(u);
            }
        }
        
        return new ResponseEntity<>(resuslt, HttpStatus.OK);

    }
    

}
