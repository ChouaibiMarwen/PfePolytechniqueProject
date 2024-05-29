package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.Ads;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Repository.Project.AdsRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdsService {

    @Autowired
    private AdsRepository repository;


    public Ads save(Ads id) {
        try {
            return this.repository.save(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public Ads update(Ads model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Ads findbyid(Long id) {
        try {
            return this.repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse findAllPg(int page, int size) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Ads> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(),pckge.getNumber(),pckge.getTotalElements(),pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }

    }


    public List<Ads> findAll() {
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



    public void deletebyid(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Long count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("opps"));
        }

    }
}
