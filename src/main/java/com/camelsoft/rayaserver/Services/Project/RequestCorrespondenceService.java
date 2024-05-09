package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.RequestCorrespondence;

import com.camelsoft.rayaserver.Repository.Project.RequestCorrespendanceRepository;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RequestCorrespondenceService {

    @Autowired
    private RequestCorrespendanceRepository repository;

    public RequestCorrespondence Save(RequestCorrespondence model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public RequestCorrespondence Update(RequestCorrespondence model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
}
