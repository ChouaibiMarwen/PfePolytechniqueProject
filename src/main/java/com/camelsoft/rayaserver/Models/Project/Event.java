package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Data
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(columnDefinition = "TEXT",name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private File_model attachment;
    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "status")
    private EventStatus status = EventStatus.PUBLISHED;

    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "archive")
    private Boolean archive = false;

    @ElementCollection
    @Column(name = "assignedto")
    private Set<RoleEnum> assignedto = new HashSet<>();


    public Event() {
        this.timestamp = new Date();
    }
    @Override
    public int hashCode() {
        return 33; // Replace with any prime number
    }
    public Event(String title, String description, Date eventDate, File_model attachment, Set<RoleEnum> assignedto, EventStatus status) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.attachment = attachment;
        this.timestamp = new Date();
        this.assignedto = assignedto;
        this.status = status;
        this.timestamp = new Date();
    }
}
