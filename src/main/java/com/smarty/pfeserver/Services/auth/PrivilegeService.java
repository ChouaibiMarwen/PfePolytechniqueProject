package com.smarty.pfeserver.Services.auth;


import com.smarty.pfeserver.Models.Auth.Privilege;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Auth.PrivilegeRepository;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
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

    public Privilege findByName(String name) {
        try {
            return this.repository.findByName(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", name));
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
