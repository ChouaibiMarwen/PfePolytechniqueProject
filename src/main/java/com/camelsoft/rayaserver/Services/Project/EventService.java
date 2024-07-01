package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Project.EventRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

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
            Page<Event> pckge = this.repository.findByArchiveIsFalseOrderByTimestampDesc(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindEventAssignedToByRole(int page, int size, RoleEnum role) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findByAssignedtoContaining(pg, role);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


        public DynamicResponse FindAllByTitlePg(int page, int size , String tit) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByTitleContainingIgnoreCaseAndArchiveIsFalseOrderByTimestampDesc(pg,tit);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public DynamicResponse FindAllByStatusPg(int page, int size , EventStatus status) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByStatusAndArchiveIsFalseOrderByTimestampDesc(pg,status);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }



    public DynamicResponse FindAllByTitleAndStatusPg(int page, int size ,String title ,EventStatus status) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findAllByTitleContainingIgnoreCaseAndStatusAndArchiveIsFalseOrderByTimestampDesc(pg, title ,status);
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


    public DynamicResponse findAllByTtileOrDatePaginated(int page, int size , String tit, Date date){

        try {
            if(tit == null && date == null)
                return  FindAllPg(page, size);
            if(tit != null && date == null)
                return FindAllByTitlePg(page, size, tit);
            if(tit == null && date != null ){
                PageRequest pg = PageRequest.of(page, size);
                Page<Event> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            if(tit != null && date != null){
                PageRequest pg = PageRequest.of(page, size);
                Page<Event> pckge = this.repository.findAllByArchiveIsFalseAndTitleContainingIgnoreCaseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,tit, date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }

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


    public List<Event> getEventsForUserList(users user) {
        return this.repository.findByArchiveIsFalseAndAssignedtoContainsOrUserseventsContains(user.getRole().getRole(),user);
    }


    public DynamicResponse getEventsForUserPg(int page, int size , users user) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Event> pckge = this.repository.findEventsByRoleOrCategoryAndArchiveIsFalse(pg,user.getRole().getRole(),user);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public DynamicResponse getComingSoonEvents(int page, int size, users user) {
        try {
            PageRequest pg = PageRequest.of(page, size);

            // Get current date and end date (one month from now)
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            Date endDate = calendar.getTime();

            Page<Event> eventsPage = repository.findComingSoonEvents(pg, currentDate, endDate, user.getRole().getRole(), user);
            return new DynamicResponse(eventsPage.getContent(), eventsPage.getNumber(), eventsPage.getTotalElements(), eventsPage.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    
}
