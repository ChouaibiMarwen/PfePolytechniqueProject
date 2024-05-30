package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;

import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

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
    private EventStatus status = EventStatus.DRAFT;

    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "archive")
    private Boolean archive = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "events_users_categories",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<UsersCategory> categories = new HashSet<>();


    @ElementCollection
    @Column(name = "assignedto")
    private Set<RoleEnum> assignedto = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "events_users",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<users> usersevents = new HashSet<>();


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File_model getAttachment() {
        return attachment;
    }

    public void setAttachment(File_model attachment) {
        this.attachment = attachment;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Set<UsersCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<UsersCategory> categories) {
        this.categories = categories;
    }

    public Set<RoleEnum> getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(Set<RoleEnum> assignedto) {
        this.assignedto = assignedto;
    }

    public void addUserEvent(users user) {
        usersevents.add(user);
        user.getEvents().add(this);
    }

    public void removeUserEvent(users user) {
        usersevents.remove(user);
        user.getEvents().remove(this);
    }

    public Set<users> getUsersevents() {
        return usersevents;
    }

    public void setUsersevents(Set<users> usersevents) {
        this.usersevents = usersevents;
    }
}
