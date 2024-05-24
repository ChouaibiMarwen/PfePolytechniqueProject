package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Repository.Project.UserCategoryRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserCategoryService {

    @Autowired
    private UserCategoryRepository repository;

    public UsersCategory Save(UsersCategory model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public UsersCategory Update(UsersCategory model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public UsersCategory FindById(Long id) {
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
            Page<UsersCategory> pckge = this.repository.findByArchiveIsFalse(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<UsersCategory> FindAllList() {
        try {
            return this.repository.findByArchiveIsFalse();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<UsersCategory> FindAllByNameList(String name) {
        try {
            return this.repository.findByArchiveIsFalseAndNameContainingIgnoreCase(name);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<UsersCategory> FindAllByCategoryRoleList(RoleEnum role) {
        try {
            return this.repository.findByArchiveIsFalseAndCategoryrole(role);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<UsersCategory> getCategoriesForUserList(Long userId) {
        return this.repository.findByUserId(userId);
    }


    public DynamicResponse getCategoriesForUserPage(int page, int size, Long userId) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<UsersCategory> pckge = this.repository.findByUserIdPg(pg, userId);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


}
