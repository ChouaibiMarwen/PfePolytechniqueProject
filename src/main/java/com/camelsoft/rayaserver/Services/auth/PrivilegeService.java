package com.camelsoft.rayaserver.Services.auth;


import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Auth.users;
import com.camelsoft.rayaserver.Repository.Auth.PrivilegeRepository;
import com.camelsoft.rayaserver.Response.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository repository;


    public Privilege save(Privilege id) {
        try {
            return this.repository.save(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }
    @Transactional
    public Privilege update(Privilege id) {
        try {
            return this.repository.save(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Privilege findbyid(Long id) {
        try {
            return this.repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }
    public DynamicResponse findAll(int page, int size) {
        try {
            PageRequest pg = PageRequest.of(page, size);

            Page<Privilege> pckge = this.repository.findAll(pg);

            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }

    }

    public List<Privilege> findAll() {
        try {
            return this.repository.findAll();
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

    public boolean existsByIdAndUser(Long id, users user) {
        try {
            return this.repository.existsByIdAndUser(id,user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }



    public boolean existsByName(String id) {
        try {
            return this.repository.existsByName(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }



}
