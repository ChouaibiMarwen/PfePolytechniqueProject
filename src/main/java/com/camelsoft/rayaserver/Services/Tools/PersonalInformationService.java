package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;

import com.camelsoft.rayaserver.Repository.Tools.PersonalInformationRepository;
import com.camelsoft.rayaserver.Response.Tools.PaginationResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PersonalInformationService {
    @Autowired
    private PersonalInformationRepository repository;
    public PersonalInformation save(PersonalInformation models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public PersonalInformation update(PersonalInformation models) {
        try {
            return this.repository.save(models);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public PersonalInformation findbyid(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public PaginationResponse FindAll(int page, int size) {
        try {

            List<PersonalInformation> result = new ArrayList<PersonalInformation>();
            Pageable paging = PageRequest.of(page, size);
            Page<PersonalInformation> pageTuts = this.repository.findAll(paging);
            result = pageTuts.getContent();
            PaginationResponse response = new PaginationResponse(
                    result,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return response;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }
}
