package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.StatisticResponse;
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

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/statistics")
public class StaticsMainController   extends BaseController {
    @Autowired
    private UserService UserServices;
    @GetMapping(value = {"/all_statistics_admin"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all statistics for admin main dashboard", notes = "Endpoint to get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, "),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<StatisticResponse> all_statistics_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) RequestState status) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        StatisticResponse response = new StatisticResponse();
        response.setTotalsupplier(0); // need update to the correct value
        response.setTotalusers(0);// need update to the correct value
        response.setTotalrevenue(0D);// need update to the correct value
        response.setTotalloanissued(0);// need update to the correct value
        response.setTotalloaninprogress(0);// need update to the correct value
        response.setTotalloandone(0);// need update to the correct value

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    //add sales by month api for the admin
}
