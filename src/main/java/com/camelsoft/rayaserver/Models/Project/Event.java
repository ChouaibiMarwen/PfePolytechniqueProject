package com.camelsoft.rayaserver.Models.Project;

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

    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "archive")
    private Boolean archive = false;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_assignedto",
            joinColumns =
            @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> assignedto = new HashSet<>();


    public Event() {
        this.timestamp = new Date();
    }

    public Event(String title, String description, Date eventDate, File_model attachment, Set<Role> assignedto) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.attachment = attachment;
        this.timestamp = new Date();
        this.assignedto = assignedto;
        this.timestamp = new Date();
    }
}
