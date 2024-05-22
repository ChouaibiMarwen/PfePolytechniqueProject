package com.camelsoft.rayaserver.Controller.Project;


import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import com.camelsoft.rayaserver.Request.project.DepartmentRequest;
import com.camelsoft.rayaserver.Services.Project.DepartmentService;
import com.camelsoft.rayaserver.Services.Project.RoleDepartmentService;
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
@RequestMapping(value = "/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleDepartmentService roleDepartmentService;


    @PostMapping(value = {"/add_department"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new department request from the admin", notes = "Endpoint to add a new department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added thedepartment"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Department> addDepartment(@ModelAttribute DepartmentRequest request) throws IOException {
        if (request.getName() == null)
            return new ResponseEntity("Department name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getRoles().isEmpty())
            return new ResponseEntity("Department's roles can't be null or empty", HttpStatus.BAD_REQUEST);

        Department  dep  = new Department(
                request.getName()
        );


         dep = this.departmentService.Save(dep);

        for (RoleDepartment r : request.getRoles()) {
            RoleDepartment roledep = new RoleDepartment();
            roledep.setDepartment(dep);
            roledep.setRolename(r.getRolename());
            this.roleDepartmentService.Save(roledep);
        }
        Department result = new Department();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
