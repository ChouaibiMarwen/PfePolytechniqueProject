package com.camelsoft.rayaserver.Models.Notification;


import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "admin_notification")
public class AdminNotification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "admin_notification_id")
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private users newuser;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private users newdriver;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private users sender;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private users reciver;
    @Column(name = "timestmp")
    private Date timestmp;

    public AdminNotification() {
        this.timestmp=new Date();
    }

    public AdminNotification(users newuser, users newdriver, users sender, users reciver) {

        this.newuser = newuser;
        this.newdriver = newdriver;
        this.sender = sender;
        this.reciver = reciver;
        this.timestmp=new Date();
    }


}
