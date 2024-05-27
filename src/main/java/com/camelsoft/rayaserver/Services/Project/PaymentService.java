package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.Payment;
import com.camelsoft.rayaserver.Repository.Project.PaymentRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repository;
    public Payment Save(Payment model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Payment Update(Payment model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Payment FindById(Long id) {
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
            Page<Payment> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public List<Payment> findAll() {
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
}
