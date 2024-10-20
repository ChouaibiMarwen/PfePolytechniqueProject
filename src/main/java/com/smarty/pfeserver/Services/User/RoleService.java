package com.smarty.pfeserver.Services.User;

import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Repository.Auth.RoleRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;
    public Role save(Role models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public Role update(Role models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public Role findbyid(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public Role findbyRole(RoleEnum role) {
        try {
            return this.repository.findByRole(role);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public boolean existsByRole(RoleEnum role) {
        try {
            return repository.existsByRole(role);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }
}
