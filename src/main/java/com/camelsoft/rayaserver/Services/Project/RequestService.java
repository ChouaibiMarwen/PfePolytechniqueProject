package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Request;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Project.RequestRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Response.Project.RequestResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    public Request Save(Request model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Request Update(Request model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Request FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse findAllByState(int page, int size, RequestState status) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<Request> pckge = this.repository.findAllByStatusAndArchiveIsFalse(pg, status);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
        public RequestResponse findAllByStateAndRole(int page, int size, RequestState status, RoleEnum role) {
        try {
            Role userRole = roleRepository.findByRole(role);
            List<Request> allrequest = new ArrayList<Request>();

            PageRequest pg = PageRequest.of(page, size);
            Page<Request> pageTuts = this.repository.findAllByStatusAndCreatorrequest_RoleAndArchiveIsFalse(pg, status, userRole);
            allrequest= pageTuts.getContent();
            RequestResponse response = new RequestResponse(
                    allrequest,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return response;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

  public RequestResponse findAllByRole(int page, int size,RoleEnum role) {
        try {

            Role userRole = roleRepository.findByRole(role);
            List<Request> allrequest = new ArrayList<Request>();

            PageRequest pg = PageRequest.of(page, size);
            Page<Request> pageTuts = this.repository.findAllByCreatorrequest_RoleAndArchiveIsFalse(pg,  userRole);
            allrequest= pageTuts.getContent();
            RequestResponse response = new RequestResponse(
                    allrequest,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return response;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }




    public List<Request> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public DynamicResponse findAllPg(int page, int size) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Request> pckge = this.repository.findAllByArchiveIsFalse(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }



    public boolean ExistById(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public void DeleteById(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

}
