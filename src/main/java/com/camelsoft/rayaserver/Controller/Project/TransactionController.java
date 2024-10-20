package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.MissionStatusEnum;
import com.camelsoft.rayaserver.Enum.TransactionEnum;
import com.camelsoft.rayaserver.Models.Project.Mission;
import com.camelsoft.rayaserver.Models.Project.Transaction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.Projet.AddTransactionRequest;
import com.camelsoft.rayaserver.Request.Projet.MissionRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.MissionService;
import com.camelsoft.rayaserver.Services.Project.TransactionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.criteria.CriteriaService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
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

@RestController
@RequestMapping(value = "api/v1/transactions")
public class TransactionController extends BaseController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;
    @Autowired
    private CriteriaService criteriaService;

    @Autowired
    private MissionService missionService;


    @PostMapping(value = {"/add_transaction"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN')")
    @ApiOperation(value = "add transaction for admin and technicians", notes = "Endpoint to Add  transaction for admin and technicians")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> add_transaction(@ModelAttribute AddTransactionRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        if (request.getAmount() == null)
            return new ResponseEntity("amount can't be null", HttpStatus.NOT_ACCEPTABLE);
        if (request.getMissionId() == null)
            return new ResponseEntity("mission id can't be null", HttpStatus.NOT_ACCEPTABLE);
        Transaction transaction = new Transaction();
        Mission mission = this.missionService.FindById(request.getMissionId());
        if (mission == null)
            return new ResponseEntity("mission not found", HttpStatus.NOT_FOUND);
        if (!this.missionService.canAddNewTransactionToMission(mission, request.getAmount()))
            return new ResponseEntity("added amount + approved and pending transactions total depass mission budget", HttpStatus.NOT_ACCEPTABLE);
        transaction.setMission(mission);
        mission.getTransactions().add(transaction);
        transaction.setCreatedby(user);
        transaction.setAmount(request.getAmount());
        if (request.getDescription() != null)
            transaction.setDescription(request.getDescription());
        if (request.getName() != null)
            transaction.setName(request.getName());

        Transaction result = this.transactionService.Save(transaction);
        return new ResponseEntity(result, HttpStatus.CREATED);
    }

/*
    @PatchMapping(value = {"/update_transaction/{transaction_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN')")
    @ApiOperation(value = "update transaction for admin and technicians", notes = "Endpoint to update  transaction for admin and technicians")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> update_transaction(@PathVariable Long transaction_id, @ModelAttribute AddTransactionRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if(transaction == null)
            return new ResponseEntity("transaction not found with that id" , HttpStatus.NOT_FOUND);

        Mission mission = this.missionService.FindById(request.getMissionId());
        if (mission == null)
            return new ResponseEntity("mission not found", HttpStatus.NOT_FOUND);
        if (!this.missionService.canAddNewTransactionToMission(mission, request.getAmount()))
            return new ResponseEntity("added amount + approved and pending transactions total depass mission budget", HttpStatus.NOT_ACCEPTABLE);
        transaction.setMission(mission);
        mission.getTransactions().add(transaction);
        transaction.setCreatedby(user);
        transaction.setAmount(request.getAmount());
        if (request.getDescription() != null)
            transaction.setDescription(request.getDescription());
        if (request.getName() != null)
            transaction.setName(request.getName());

        Transaction result = this.transactionService.Save(transaction);
        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    */



    @GetMapping(value = {"/all_transactions_list"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "all transactions list for admin", notes = "Endpoint to get all transactions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<Transaction>> all_transaction_list(@RequestParam(required = false) String missionTitle,@RequestParam(required = false) Long missionId,@RequestParam(required = false) Long technicianId,@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate ) throws IOException {
        List<Transaction> result = this.criteriaService.getFilteredTransactions(missionTitle,missionId, technicianId,startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_transactions_pg"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "all transactions pagination for admin", notes = "Endpoint to get all transactions for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> all_transaction_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,@RequestParam(required = false) String missionTitle,@RequestParam(required = false) Long missionId,@RequestParam(required = false) Long technicianId,@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate ) throws IOException {
        List<Transaction> result = this.criteriaService.getFilteredTransactions(missionTitle,missionId, technicianId,startDate, endDate);
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), result.size());
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<Transaction> listPage = new PageImpl<>(result.subList(start, end), pageable, result.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);
    }



    @GetMapping(value = {"/my_transactions_pg"})
    @PreAuthorize("hasRole('TECHNICIEN')")
    @ApiOperation(value = "all transactions pagination for currentuser", notes = "Endpoint to get all transactions for currentuser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<DynamicResponse> my_transaction_pg(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,@RequestParam(required = false) String missionTitle,@RequestParam(required = false) Long missionId,@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate ) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        List<Transaction> result = this.criteriaService.getFilteredTransactions(missionTitle,missionId, currentuser.getId(),startDate, endDate);
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), result.size());
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<Transaction> listPage = new PageImpl<>(result.subList(start, end), pageable, result.size());
        DynamicResponse dynamicresponse = new DynamicResponse(listPage.getContent(), listPage.getNumber(), listPage.getTotalElements(), listPage.getTotalPages());
        return new ResponseEntity<>(dynamicresponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/my_transactions_list"})
    @PreAuthorize("hasRole('TECHNICIEN')")
    @ApiOperation(value = "all transactions list for currentuser", notes = "Endpoint to get all transactions for currentuser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<Transaction>> my_transactions_list(@RequestParam(required = false) String missionTitle,@RequestParam(required = false) Long missionId,@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate ) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        List<Transaction> result = this.criteriaService.getFilteredTransactions(missionTitle,missionId, currentuser.getId(),startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/transaction_by_id/{transaction_id}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIEN') ")
    @ApiOperation(value = "get a transaction sby id for admin and users", notes = "Endpoint to get a transaction sby id for admin and users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> transaction_by_id( @PathVariable Long transaction_id) throws IOException {
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if (transaction == null)
            return new ResponseEntity("transaction not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(transaction, HttpStatus.OK);

    }

    @GetMapping(value = {"/update_transaction_status/{transaction_id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "update a transaction status for admin", notes = "Endpoint to update a transaction status for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> update_transaction_status(@PathVariable Long transaction_id, @RequestParam TransactionEnum status) throws IOException {
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if (transaction == null)
            return new ResponseEntity("transaction not found", HttpStatus.NOT_FOUND);
        transaction.setStatus(status);
        Transaction result = this.transactionService.Update(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PatchMapping(value = {"/cancel_transaction_before_approve_or_rejection/{transaction_id}"})
    @PreAuthorize("hasRole('TECHNICIEN') ")
    @ApiOperation(value = "cancel a transaction for technician", notes = "Endpoint to cancel a transaction for technician")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> cancel_transaction_before_approve_or_rejection(@PathVariable Long transaction_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if (transaction == null)
            return new ResponseEntity("transaction not found", HttpStatus.NOT_FOUND);
        if(transaction.getCreatedby() != currentuser)
            return new ResponseEntity("you can't cancel a transaction created by other user", HttpStatus.FORBIDDEN);
        if(transaction.getStatus() != TransactionEnum.PENDING)
            return new ResponseEntity("only pending transactions can be cancelled", HttpStatus.FORBIDDEN);
        transaction.setStatus(TransactionEnum.CANCELLED);
        Transaction result = this.transactionService.Update(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PatchMapping(value = {"/approve_transaction_by_teamlead/{transaction_id}"})
    @PreAuthorize("hasRole('TECHNICIEN') ")
    @ApiOperation(value = "approve a transaction for technician teamlead", notes = "Endpoint to approve a transaction for technician teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> approve_transaction_by_teamlead(@PathVariable Long transaction_id) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if (transaction == null)
            return new ResponseEntity("transaction not found", HttpStatus.NOT_FOUND);
        Mission mission = transaction.getMission();
        if(currentuser != mission.getTeamLead())
            return new ResponseEntity("only mission teamlead can approve this transaction", HttpStatus.NOT_FOUND);
        if(transaction.getStatus() == TransactionEnum.CANCELLED)
            return new ResponseEntity("this transaction is canceled by the technician that create", HttpStatus.FORBIDDEN);
        transaction.setStatus(TransactionEnum.APPROVED);
        Transaction result = this.transactionService.Update(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PatchMapping(value = {"/reject_transaction_by_teamlead/{transaction_id}"})
    @PreAuthorize("hasRole('TECHNICIEN') ")
    @ApiOperation(value = "approve a transaction for technician teamlead", notes = "Endpoint to approve a transaction for technician teamlead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check required fields"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Transaction> reject_transaction_by_teamlead(@PathVariable Long transaction_id, @RequestParam(required = false) String reason) throws IOException {
        users currentuser = this.userService.findByUserName(getCurrentUser().getUsername());
        Transaction transaction = this.transactionService.FindById(transaction_id);
        if (transaction == null)
            return new ResponseEntity("transaction not found", HttpStatus.NOT_FOUND);
        Mission mission = transaction.getMission();
        if(currentuser != mission.getTeamLead())
            return new ResponseEntity("only mission teamlead can reject this transaction", HttpStatus.NOT_FOUND);
        if(transaction.getStatus() == TransactionEnum.CANCELLED)
            return new ResponseEntity("this transaction is canceled by the technician that create", HttpStatus.FORBIDDEN);
        transaction.setStatus(TransactionEnum.REJECTED);
        if(reason != null){
            transaction.setRejectionreason(reason);
        }
        Transaction result = this.transactionService.Update(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
