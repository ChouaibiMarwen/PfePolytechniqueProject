package com.smarty.pfeserver.Services.Project;

import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Project.BoostBudgetRequestReposirtory;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BoostBudgetRequestService {

    @Autowired
    private BoostBudgetRequestReposirtory repository;

    public BoostBudgetRequest Save(BoostBudgetRequest model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public BoostBudgetRequest Update(BoostBudgetRequest model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public BoostBudgetRequest FindById(Long id) {
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
            Page<BoostBudgetRequest> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public List<BoostBudgetRequest> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public List<BoostBudgetRequest> findUserRequests(users user) {
        return this.repository.findAllByCreatedbyOrderByTimestampDesc(user);
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

    public Integer CountByStatus(TransactionEnum status) {
        return this.repository.countByStatus(status);
    }

}
