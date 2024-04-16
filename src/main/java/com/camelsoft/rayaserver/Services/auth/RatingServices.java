package com.camelsoft.rayaserver.Services.auth;


import com.camelsoft.rayaserver.Models.Auth.Rating;
import com.camelsoft.rayaserver.Models.Auth.Supplier;
import com.camelsoft.rayaserver.Models.Auth.users;
import com.camelsoft.rayaserver.Repository.Auth.RatingRepository;
import com.camelsoft.rayaserver.Response.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RatingServices {


    @Autowired
    private RatingRepository repository;


    public Rating save(Rating id) {
        try {
            return this.repository.save(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }

    public Rating findbyid(Long id) {
        try {
            return this.repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }


    public Boolean existbyUserAndSupplier(users id, Supplier product) {
        try {
            return this.repository.existsByUserAndSupplier(id,product);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }
    }


    public List<Rating> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }
    }



    public Long countPositive() {
        try {
            return this.repository.getSumOfPositiveRatingsForSupplier();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }
    }



    public Long countBad() {
        try {
            return this.repository.getSumOfBadRatingsForSupplier();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base"));
        }
    }



    public DynamicResponse findAllSupplierRating(int page, int size, Supplier supplier) {
        try {
            Page<Rating> ratings = this.repository.findAllBySupplierOrderByTimestampDesc(PageRequest.of(page, size),supplier);
            return new DynamicResponse(ratings.getContent(),ratings.getNumber(), ratings.getTotalElements(), ratings.getTotalPages());
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

}
