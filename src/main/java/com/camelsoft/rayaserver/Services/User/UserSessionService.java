package com.camelsoft.rayaserver.Services.User;


import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.User.UserSessionRepository;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserSessionService {
    @Autowired
    private UserSessionRepository repository;

    public UserSession save(UserSession models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public UserSession update(UserSession models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public UserSession findbyid(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public PaginationResponse FindAll(int page, int size) {
        try {

            List<UserSession> result = new ArrayList<UserSession>();
            Pageable paging = PageRequest.of(page, size);
            Page<UserSession> pageTuts = this.repository.findAll(paging);
            result = pageTuts.getContent();
            PaginationResponse response = new PaginationResponse(
                    result,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return response;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }
 public PaginationResponse FindAllByUser(int page, int size, users user) {
        try {

            List<UserSession> result = new ArrayList<UserSession>();
            Pageable paging = PageRequest.of(page, size);
            Page<UserSession> pageTuts = this.repository.findAllByUser(paging,user);
            result = pageTuts.getContent();
            PaginationResponse response = new PaginationResponse(
                    result,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return response;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }
}
