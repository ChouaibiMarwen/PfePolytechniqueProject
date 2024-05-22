package com.camelsoft.rayaserver.Services.User;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.User.UserActionRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserActionService {
    
    @Autowired
    private UserActionRepository repository;



    public UserAction Save(UserAction model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public UserAction Update(UserAction model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public UserAction FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public DynamicResponse FindAllBySubAdmin(int page, int size) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<UserAction> pckge = this.repository.findByUserRoleRole(pg, RoleEnum.ROLE_SUB_ADMIN);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindLastActionsForAllSubAdmins(int page, int size) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<UserAction> pckge = this.repository.findLatestUserActionsByRole(pg, RoleEnum.ROLE_SUB_ADMIN);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllSubAdminAction(int page, int size, users user) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<UserAction> pckge = this.repository.findByUserOrderByTimestampDesc(pg, user);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public DynamicResponse FindAllSubAdminActionByUserId(int page, int size,Long idUser) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<UserAction> pckge = this.repository.findLatestUserActionsByRoleAndUserId(pg, RoleEnum.ROLE_SUB_ADMIN, idUser);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllSubAdminActionByName(int page, int size,String name) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<UserAction> pckge = this.repository.findLatestUserActionsByRoleAndName(pg, RoleEnum.ROLE_SUB_ADMIN, name);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }




}
