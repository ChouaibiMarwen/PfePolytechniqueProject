package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.Service_Agreement;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Repository.Project.ServiceAgreementRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceAgreementService {
    
    @Autowired
    private ServiceAgreementRepository repository;


    public Service_Agreement Save(Service_Agreement model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Service_Agreement Update(Service_Agreement model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public void deleteServiceAgreement(Service_Agreement model) {
        this.repository.delete(model);
    }


    public Service_Agreement FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse FindAllPg(int page, int size) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<Service_Agreement> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public DynamicResponse findByPurchaseorderPg(int page, int size, PurshaseOrder po) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Service_Agreement> pckge = this.repository.findByPurchaseorderAndDeletedIsFalse(pg,po);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public List<Service_Agreement> findAllByPurchaseorder(PurshaseOrder po) {
        try {

            List<Service_Agreement> pckge = this.repository.findByPurchaseorderAndDeletedIsFalse(po);
            return pckge;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }



    public List<Service_Agreement> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<Service_Agreement> findAllByName(String name) {
        try {
            return this.repository.findAllByTitleContainingIgnoreCaseAndDeletedIsFalse(name);
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

}
