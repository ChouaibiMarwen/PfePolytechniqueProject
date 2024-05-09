package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Repository.Project.EventRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class EventService {
    
    @Autowired
    private EventRepository repository;

    public Event Save(Event model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Event Update(Event model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Event FindById(Long id) {
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
            Page<Event> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
        public DynamicResponse FindAllByTitlePg(int page, int size , String tit) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByTitleContainingIgnoreCaseAndArchiveIsFalse(pg,tit);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public DynamicResponse FindAllByStatusPg(int page, int size , EventStatus status) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByStatusAndArchiveIsFalse(pg,status);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }



    public DynamicResponse FindAllByTitleAndStatusPg(int page, int size ,String title ,EventStatus status) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByTitleContainingIgnoreCaseAndStatusAndArchiveIsFalse(pg, title ,status);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public DynamicResponse findAllByTtileOrStatusPaginated(int page, int size , String tit, EventStatus status){

        try {
            if(tit == null && status == null)
                return  FindAllPg(page, size);
            if(tit != null && status == null)
                return FindAllByTitlePg(page, size, tit);
            if(tit == null && status != null )
                return  FindAllByStatusPg(page, size, status);
            if(tit != null && status != null)
                return FindAllByTitleAndStatusPg(page, size,tit,status);

            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<Event> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<Event> findAllByName(String name) {
        try {
            return this.repository.findAllByTitleContainingIgnoreCaseAndArchiveIsFalse(name);
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


    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    
}
