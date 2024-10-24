package com.smarty.pfeserver.Controller.Project;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Response.Project.IaResponse;
import com.smarty.pfeserver.Services.IaServices.OpenAIService;
import com.smarty.pfeserver.Services.IaServices.PredectionsServices;
import com.smarty.pfeserver.Services.User.UserService;
import com.smarty.pfeserver.Tools.Util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/iatool")
public class IaController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private PredectionsServices predectionsServices;

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/company_future_prediction")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    public ResponseEntity<IaResponse> company_future_prediction(
            @RequestParam Integer participantsnumber,
            @RequestParam String missiondescription ) throws FirebaseMessagingException {

        users admin = this.userService.findByUserName(getCurrentUser().getUsername());

        String resp = this.predectionsServices.decideMissionParticipantsBudget( missiondescription, participantsnumber);
        IaResponse result = new IaResponse(resp);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    /*@PostMapping("/get_ia_models_list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    public ResponseEntity<String> get_ia_models_list() throws FirebaseMessagingException {
        users admin = this.userService.findByUserName(getCurrentUser().getUsername());
        return new ResponseEntity<>(this.openAIService.listModels(), HttpStatus.OK);
    }*/

}
