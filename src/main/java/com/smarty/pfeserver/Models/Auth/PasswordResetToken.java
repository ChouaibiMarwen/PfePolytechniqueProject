package com.smarty.pfeserver.Models.Auth;

import com.smarty.pfeserver.Models.User.users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken implements Serializable {
    private static final int EXPIRATION = 180;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "reset_id" )
    private Long id;
    @Column(name = "reset_token")
    private String token;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH},targetEntity = users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private users user;
    @Column(name = "reset_expirydate")
    private Date expiryDate;

    public PasswordResetToken() {
        this.expiryDate = this.addHoursToJavaUtilDate(new Date(),EXPIRATION);
    }

    public PasswordResetToken(String token, users user) {
        this.token = token;
        this.user = user;
        this.expiryDate = this.addHoursToJavaUtilDate(new Date(),EXPIRATION);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public users getUser() {
        return user;
    }

    public void setUser(users user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Date addHoursToJavaUtilDate(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }
}
