package com.camelsoft.rayaserver.Controller.Project;


import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Services.Project.DepartmentService;
import com.camelsoft.rayaserver.Services.Project.RoleDepartmentService;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/department")
public class DepartmentController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserActionService userActionService;

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
    public ResponseEntity<Department> addDepartment(@RequestParam String name , @RequestParam List<String> roledepartmentNames ) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        if (name.length() == 0)
            return new ResponseEntity("Department name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (roledepartmentNames.isEmpty())
            return new ResponseEntity("Department's roles can't be null or empty", HttpStatus.BAD_REQUEST);

        Department  dep  = new Department(
                name
        );


         dep = this.departmentService.Save(dep);

        for (String r : roledepartmentNames) {
            RoleDepartment roledep = new RoleDepartment();
            roledep.setDepartment(dep);
            roledep.setRolename(r);
            this.roleDepartmentService.Save(roledep);
        }
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.DEPARTMENT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        Department result =this.departmentService.FindById(dep.getId());
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
    public ResponseEntity<Department> updatDepartment(@PathVariable Long idDepartment, @RequestParam(required = false) String name, @RequestParam(required = false)List<String> rolesDepartmentname) throws IOException {
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Department dep =  this.departmentService.FindById(idDepartment);
        if(dep == null)
            return new ResponseEntity("department is not founded  by this id : " + idDepartment , HttpStatus.NOT_FOUND);

        if(name != null && name.length() > 0)
            dep.setName(name);
        if(rolesDepartmentname != null && !rolesDepartmentname.isEmpty()){
            for(RoleDepartment r : dep.getRoles()){
                dep.getRoles().remove(r);
                r.setArchive(true);
                roleDepartmentService.Save(r);
            }
            for (String r : rolesDepartmentname) {
                RoleDepartment roledep = new RoleDepartment();
                roledep.setDepartment(dep);
                roledep.setRolename(r);
                this.roleDepartmentService.Save(roledep);
            }
        }
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.DEPARTMENT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
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
        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Department dep =  this.departmentService.FindById(idDepartment);
        if(dep == null)
            return new ResponseEntity("department is not founded  by this id : " + idDepartment , HttpStatus.NOT_FOUND);

       dep.setArchive(true);
        Department result = this.departmentService.Update(dep);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.DEPARTMENT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = {"/all_departments"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all departments for admin", notes = "Endpoint to get all departments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Department>> all_departments_list() throws IOException {
        List<Department> result = this.departmentService.findAllNotArchived();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
