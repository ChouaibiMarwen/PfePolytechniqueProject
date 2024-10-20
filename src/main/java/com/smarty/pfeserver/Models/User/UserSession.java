package com.smarty.pfeserver.Models.User;



import com.smarty.pfeserver.Enum.User.SessionAction;
import com.smarty.pfeserver.Models.Auth.UserDevice;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserSession")
public class UserSession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private users user;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private UserDevice device;
    @Column(name = "action")
    private SessionAction action;
    @Column(name = "comment")
    private String comment;
    @Column(name = "timestmp")
    private Date timestmp;

    public UserSession() {
        this.timestmp=new Date();
    }

    public UserSession(users user, UserDevice device, SessionAction action) {
        this.user = user;
        this.device = device;
        this.action = action;
        this.timestmp=new Date();
    }

    public UserSession(users user, UserDevice device, SessionAction action, String comment) {
        this.user = user;
        this.device = device;
        this.action = action;
        this.comment = comment;
        this.timestmp=new Date();
    }
   public UserSession(users user, SessionAction action, String comment) {
        this.user = user;

        this.action = action;
        this.comment = comment;
        this.timestmp=new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public users getUser() {
        return user;
    }

    public void setUser(users user) {
        this.user = user;
    }

    public UserDevice getDevice() {
        return device;
    }

    public void setDevice(UserDevice device) {
        this.device = device;
    }

    public SessionAction getAction() {
        return action;
    }

    public void setAction(SessionAction action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
