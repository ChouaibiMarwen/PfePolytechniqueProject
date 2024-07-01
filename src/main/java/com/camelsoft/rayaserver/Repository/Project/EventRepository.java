package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByTitleContainingIgnoreCaseAndArchiveIsFalse(String name);
    Page<Event> findAllByTitleContainingIgnoreCaseAndArchiveIsFalseOrderByTimestampDesc(Pageable page, String name);
    Page<Event> findByArchiveIsFalseOrderByTimestampDesc(Pageable page);
    Page<Event> findAllByTitleContainingIgnoreCaseAndStatusAndArchiveIsFalseOrderByTimestampDesc(Pageable page, String name, EventStatus status);
    Page<Event> findAllByArchiveIsFalseAndTitleContainingIgnoreCaseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page, String name, Date date);
    Page<Event> findAllByStatusAndArchiveIsFalseOrderByTimestampDesc(Pageable page, EventStatus status);
    Page<Event> findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page, Date date);

    Page<Event> findByAssignedtoContaining(Pageable page, RoleEnum role);

    List<Event> findByArchiveIsFalseAndAssignedtoContainsOrUserseventsContains(RoleEnum role, users user);
    Page<Event> findByArchiveIsFalseAndAssignedtoContainsOrUserseventsContains(Pageable page , RoleEnum role, users user);

    @Query("SELECT e FROM Event e WHERE e.archive = false AND e.eventDate BETWEEN :currentDate AND :endDate AND (:role MEMBER OF e.assignedto OR :user MEMBER OF e.usersevents)")
    Page<Event> findComingSoonEvents(Pageable pageable, Date currentDate, Date endDate, RoleEnum role, users user);



}


