package com.smarty.pfeserver.Controller.Project;


import com.google.api.Http;
import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Project.Task;
import com.smarty.pfeserver.Models.Project.Transaction;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Request.Projet.AddTransactionRequest;
import com.smarty.pfeserver.Request.Projet.TaskRequest;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Services.Project.MissionService;
import com.smarty.pfeserver.Services.Project.TaskService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/tasks")
public class TaskController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CriteriaService criteriaService;

    @Autowired
    private MissionService missionService;
    @Autowired
    private TaskService taskService;

    @PostMapping(value = {"/add_task/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN')")
    @ApiOperation(value = "add task for supervisor and admin", notes = "Endpoint to add task for supervisor and admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Task> add_task(@PathVariable Long mission_id, @ModelAttribute TaskRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null) {
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        }
        Task task = new Task();
        task.setCreatedby(user);
        if(request.getStartdate() != null)
            task.setStartdate(request.getStartdate());
        if(request.getEnddate() != null)
            task.setEnddate(request.getEnddate());
        if(request.getDescription() != null)
            task.setDescription(request.getDescription());
        if(request.getTitle() != null)
            task.setTitle(request.getTitle());
        task.setMission(mission);
        mission.getTasks().add(task);
        if(request.getIdParticipants() != null && !request.getIdParticipants().isEmpty()) {
            for (Long id : request.getIdParticipants()) {
                users u = this.userService.findById(id);
                if (u == null) {
                    return new ResponseEntity("User with ID " + id + " not found", HttpStatus.NOT_FOUND);
                }
                task.addParticipants(u);
            }
        }
        Task result = this.taskService.Save(task);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_tasks_pg"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all tasks for admin", notes = "Endpoint to get missions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> all_missall_tasks_pgions_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title, @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate, @RequestParam(required = false) MissionStatusEnum status) throws IOException {

        List<Task> mymissions = this.criteriaService.findTasksWithFilters(title, startdate, enddate,status, null);

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), mymissions.size());
        int end = Math.min((start + pageable.getPageSize()), mymissions.size());
        Page<Task> listPage = new PageImpl<>(mymissions.subList(start, end), pageable, mymissions.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/my_tasks_pg"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all tasks for technician", notes = "Endpoint to all tasks for technicien")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> my_tasks_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title, @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate, @RequestParam(required = false) MissionStatusEnum status) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        List<Task> mymissions = this.criteriaService.findTasksWithFilters(title, startdate, enddate,status, user);

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), mymissions.size());
        int end = Math.min((start + pageable.getPageSize()), mymissions.size());
        Page<Task> listPage = new PageImpl<>(mymissions.subList(start, end), pageable, mymissions.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/my_tasks_by_mission/{mission_id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all tasks for technician", notes = "Endpoint to all tasks for technicien")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> my_tasks_by_mission(@PathVariable Long mission_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String title, @RequestParam(required = false) Date startdate, @RequestParam(required = false) Date enddate, @RequestParam(required = false) MissionStatusEnum status) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        Mission mission = this.missionService.FindById(mission_id);
        if (mission == null) {
            return new ResponseEntity("Mission not found", HttpStatus.NOT_FOUND);
        }
        List<Task> mymissions = user.getTasks().stream()
                .filter( t-> t.getMission().getId() == mission_id ).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), mymissions.size());
        int end = Math.min((start + pageable.getPageSize()), mymissions.size());
        Page<Task> listPage = new PageImpl<>(mymissions.subList(start, end), pageable, mymissions.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);

    }

    @PatchMapping(value = {"/update_task_status/{task_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "update a task status for admin and users", notes = "Endpoint to update a task for admin and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Task> all_my_notification( @PathVariable Long task_id,@RequestParam MissionStatusEnum status) throws IOException {
        Task task = this.taskService.FindById(task_id);
        if (task == null)
            return new ResponseEntity("task not found", HttpStatus.NOT_FOUND);
        task.setStatus(status);
        Task result = this.taskService.Update(task);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    @PatchMapping(value = {"/update_task_realisation_percentage/{task_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "update a task realisation_percentage for admin and users", notes = "Endpoint to update realisation_percentage for admin and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Task> update_task_realisation_percentage( @PathVariable Long task_id,@RequestParam Double percentage) throws IOException {
        if(percentage > 100 ||percentage < 0)
            return new ResponseEntity("wrong percentage" , HttpStatus.NOT_ACCEPTABLE);
        Task task = this.taskService.FindById(task_id);
        if (task == null)
            return new ResponseEntity("task not found", HttpStatus.NOT_FOUND);
        task.setEvolutionPercentage(percentage);
        Task result = this.taskService.Update(task);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/task_by_id/{task_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get task for admin and users", notes = "Endpoint to get task for admin and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Task> task_by_id( @PathVariable Long task_id) throws IOException {
        Task result = this.taskService.FindById(task_id);
        if (result == null)
            return new ResponseEntity("task not found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
