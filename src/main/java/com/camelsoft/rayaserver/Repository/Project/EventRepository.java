package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByTitleContainingIgnoreCaseAndArchiveIsFalse(String name);
    Page<Event> findAllByTitleContainingIgnoreCaseAndArchiveIsFalse(Pageable page, String name);
    Page<Event> findAllByTitleContainingIgnoreCaseAndStatusAndArchiveIsFalse(Pageable page, String name, EventStatus status);
    Page<Event> findAllByStatusAndArchiveIsFalse(Pageable page, EventStatus status);
}
