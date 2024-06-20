package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Repository.Project.SupplierClassificationRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SupplierClassificationService {

    @Autowired
    private SupplierClassificationRepository repository;


    public SuppliersClassification Save(SuppliersClassification model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public SuppliersClassification Update(SuppliersClassification model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public SuppliersClassification FindById(Long id) {
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
            Page<SuppliersClassification> pckge = this.repository.findByArchiveIsFalse(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllByNamePg(int page, int size, String name) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<SuppliersClassification> pckge = this.repository.findByArchiveIsFalseAndNameContainingIgnoreCase(pg, name);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<SuppliersClassification> FindAllList() {
        try {
            return this.repository.findByArchiveIsFalse();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<SuppliersClassification> FindAllByNameList(String name) {
        try {

            return this.repository.findByArchiveIsFalseAndNameContainingIgnoreCase(name);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public List<SuppliersClassification> FindAll() {
        try {

            return this.repository.findByArchiveIsFalse();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public boolean existbyname(String name) {
        try {
            return this.repository.existsByNameIgnoreCase(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }




   /* public List<SuppliersClassification> FindAllByCategoryRoleList(RoleEnum role) {
        try {
            return this.repository.findByArchiveIsFalseAndCategoryrole(role);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
*/
    /*public List<SuppliersClassification> getCategoriesForUserList(Long userId) {
        return this.repository.findByUserId(userId);
    }


    public DynamicResponse getCategoriesForUserPage(int page, int size, Long userId) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<SuppliersClassification> pckge = this.repository.findByUserIdPg(pg, userId);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }*/
}
