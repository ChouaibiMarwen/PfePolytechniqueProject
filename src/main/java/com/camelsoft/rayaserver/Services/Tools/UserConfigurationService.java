package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.UserConfiguration;
import com.camelsoft.rayaserver.Repository.Project.RayaSettingRepository;
import com.camelsoft.rayaserver.Repository.Tools.UserConfigurationRepository;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserConfigurationService {

    @Autowired
    private UserConfigurationRepository repository;


    public UserConfiguration Save(UserConfiguration model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public UserConfiguration Update(UserConfiguration model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public UserConfiguration FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

}
