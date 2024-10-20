package com.smarty.pfeserver.Services.Tools;

import com.smarty.pfeserver.Models.Tools.RayaSettings;
import com.smarty.pfeserver.Repository.Project.RayaSettingRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RayaSettingService {
    @Autowired
    private RayaSettingRepository repository;


    public RayaSettings Save(RayaSettings model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public RayaSettings Update(RayaSettings model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public RayaSettings FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }
    public RayaSettings FindFirst() {
        try {
                return this.repository.findFirstByOrderByTimestampDesc();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
}
