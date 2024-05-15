package com.camelsoft.rayaserver.Services.User;


import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Tools.PersonalInformationRepository;
import com.camelsoft.rayaserver.Repository.User.SupplierRepository;
import com.camelsoft.rayaserver.Repository.User.UserRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SupplierServices {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalInformationRepository personalInformationRepository;
    @Autowired
    private RoleRepository roleRepository;


    public Supplier save(Supplier model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public Supplier update(Supplier model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public Supplier findbyid(Long id) {
        try {
            return this.repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }

    public List<Supplier> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }
    }

    public Long countAll() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }
    }

    public boolean existbyid(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }

    public void deletebyid(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }

    public List<UserShortDto> getAllUsersWithoutPagination(Boolean active, String name, RoleEnum role, Boolean verified) {
        try {
            Role userRole = roleRepository.findByRole(role);
            List<users> user = null;
            if (name == null && active == null && verified == null)
                user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(userRole, true, false);
            else if (name == null && active == null && verified != null)
                user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, true, false, verified);
            else if (name != null && active == null && verified == null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(userRole, "%" + name + "%", false, "%DELETED%");
            else if (name != null && active == null && verified != null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndVerifiedOrderByTimestmpDesc(userRole, "%" + name + "%", false, "%DELETED%", verified);
            else if (name == null && active != null && verified == null)
                user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(userRole, active, false);
            else if (name == null && active != null && verified != null)
                user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, active, false, verified);
            else if (name != null && active != null && verified == null)
                user = this.userRepository.findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(userRole, active, "%" + name + "%", false);
            else if (name != null && active != null && verified != null)
                user = this.userRepository.findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, active, "%" + name + "%", false, verified);
            return  user.stream()
                    .map(UserShortDto::mapToUserShortDTO)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }


    public Long countSuppliers() {
        return this.repository.count();
    }


   public DynamicResponse DynamicResponsefindyNameorPurshaseOrderStatus(int page,int size, String tit, PurshaseOrderStatus status){

       try {
           if(tit == null && status == null){
               PageRequest pg = PageRequest.of(page, size);
               Page<Supplier> pckge = this.repository.findAll(pg);
               return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
           }
              // return  findAll(page, size);
           if(tit != null && status == null){
               PageRequest pg = PageRequest.of(page, size);
               Page<users> pckge = this.repository.findUsersByNameContaining(pg,tit);
               return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
           }
              // return FindAllByTitlePg(page, size, tit);
           if(tit == null && status != null ){
               PageRequest pg = PageRequest.of(page, size);
               Page<users> pckge = this.repository.findUsersByPurchaseOrderStatus(pg,status);
               return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
           }
           if(tit != null && status != null){
               PageRequest pg = PageRequest.of(page, size);
               Page<users> pckge = this.repository.findUsersByNameAndPurchaseOrderStatus(pg,tit, status);
               return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
           }

           return null;
       } catch (NoSuchElementException ex) {
           throw new NotFoundException(ex.getMessage());
       }

   }


    public DynamicResponse getAllSuppliersHavingAvailbalVeheclesStock(int page,int size){
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Supplier> pckge = this.repository.findSuppliersWithAvailableVehicles(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }



}
