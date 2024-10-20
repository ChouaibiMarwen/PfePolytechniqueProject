package com.smarty.pfeserver.Services.Project;

import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Project.Transaction;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Project.MissionRepository;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MissionService {

    @Autowired
    private MissionRepository repository;
    @Autowired
    private TransactionService transactionService;
    public Mission Save(Mission model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Mission Update(Mission model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Mission FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllPg(int page, int size) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Mission> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public List<Mission> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public boolean ExistById(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public boolean hasMissionInPeriod(Date startDate, Date endDate, users user) {
        Set<Mission> missions = user.getMissions();
        for (Mission mission : missions) {
            if (isDateRangeOverlapping(startDate, endDate, mission.getStartdate(), mission.getEnddate())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDateRangeOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return (start1.before(end2) || start1.equals(end2)) && (end1.after(start2) || end1.equals(start2));
    }


    public Double countNowTotalTransactionsMission(Mission mission) {
        List<Transaction> missionTransactions = mission.getTransactions().stream()
                .filter(t -> t.getStatus() == TransactionEnum.PENDING || t.getStatus() == TransactionEnum.APPROVED)
                .collect(Collectors.toList());
        Double total = 0.0;
        for(Transaction tran : missionTransactions){
            total += tran.getAmount();
        }

        return total;
    }

    public Boolean canAddNewTransactionToMission(Mission mission, Double amount){

        Double totaltransactionsamount = this.countNowTotalTransactionsMission(mission);
        if(totaltransactionsamount + amount > amount)
            return false;
        else
            return true;

    }
}
