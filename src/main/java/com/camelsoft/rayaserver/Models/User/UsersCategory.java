package com.camelsoft.rayaserver.Models.User;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
@Table(name = "Category")
public class UsersCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(columnDefinition = "TEXT",name = "description")
    private String description;
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "categoryrole")
    private RoleEnum categoryrole;
    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private Set<users> users = new HashSet<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

    public UsersCategory() {
        this.timestamp = new Date();
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public RoleEnum getCategoryrole() {
        return categoryrole;
    }

    public void setCategoryrole(RoleEnum categoryrole) {
        this.categoryrole = categoryrole;
    }


    public void addUser(users user) {
        users.add(user);
        user.getCategories().add(this);
    }

    public void removeUser(users user) {
        users.remove(user);
        user.getCategories().remove(this);
    }


    public Set<users> getUsers() {
        return users;
    }

    public void setUsers(Set<users> users) {
        this.users = users;
    }
}
