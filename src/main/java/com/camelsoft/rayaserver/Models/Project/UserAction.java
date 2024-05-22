package com.camelsoft.rayaserver.Models.Project;


import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USER_Action")
public class UserAction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "action")
    private UserActionsEnum action;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id_action")
    private users user;
    @Column(name = "timestamp")
    private Date timestamp;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;

    public UserAction(){
        this.timestamp = new Date();
    }

    public UserAction(UserActionsEnum action, users user) {
        this.action = action;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserActionsEnum getAction() {
        return action;
    }

    public void setAction(UserActionsEnum action) {
        this.action = action;
    }

    public users getUser() {
        return user;
    }

    public void setUser(users user) {
        this.user = user;
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
}
