package com.camelsoft.rayaserver.Controller.Project;


import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import com.camelsoft.rayaserver.Request.project.DepartmentRequest;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

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


    @GetMapping(value = {"/depatment/{id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get department by id for admin ", notes = "Endpoint to get department by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Department> getdepartmentById(@PathVariable Long id) throws IOException {
        Department result = this.departmentService.FindById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = {"/update_department/{idDepartment}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "update a department from the admin", notes = "Endpoint to update a department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the department"),
            @ApiResponse(code = 400, message = "Bad request, check params type "),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Department> updatDepartment(@PathVariable Long idDepartment, @RequestParam(required = false) String name, @RequestParam(required = false)Set<RoleDepartment> rolesDepartment) throws IOException {

        Department dep =  this.departmentService.FindById(idDepartment);
        if(dep == null)
            return new ResponseEntity("department is not founded  by this id : " + idDepartment , HttpStatus.NOT_FOUND);

        if(name != null && name.length() > 0)
            dep.setName(name);
        if(rolesDepartment != null && rolesDepartment.size() > 0){
            for(RoleDepartment r : dep.getRoles()){
                dep.getRoles().remove(r);
                r.setDepartment(null);
                roleDepartmentService.Save(r);
            }
            for(RoleDepartment r :rolesDepartment){
                r.setDepartment(dep);
                roleDepartmentService.Save(r);
            }
        }

        Department result = this.departmentService.Update(dep);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PatchMapping(value = {"/delete_department/{idDepartment}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "update a department from the admin", notes = "Endpoint to update a department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the department"),
            @ApiResponse(code = 400, message = "Bad request, check params type "),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Department> deleteDepartment(@PathVariable Long idDepartment) throws IOException {

        Department dep =  this.departmentService.FindById(idDepartment);
        if(dep == null)
            return new ResponseEntity("department is not founded  by this id : " + idDepartment , HttpStatus.NOT_FOUND);

       dep.setArchive(true);
        Department result = this.departmentService.Update(dep);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
