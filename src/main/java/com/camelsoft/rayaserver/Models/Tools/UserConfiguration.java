package com.camelsoft.rayaserver.Models.Tools;

import com.camelsoft.rayaserver.Enum.Tools.Language;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "UserConfiguration")
public class UserConfiguration  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "configuration_id")
    private Long id;
    @Column(name = "notification_email")
    private Boolean notificationemail = false;
    @Column(name = "notification_fcm")
    private Boolean notificationfcm = false;
    @Column(name = "language")
    private Language language = Language.ENGLISH;
    @Column(name = "timestmp")
    private Date timestmp = new Date();
    @OneToOne(mappedBy = "userconfiguration")
    @JsonIgnore
    private users user;
    public UserConfiguration() {

    }

    public UserConfiguration(Boolean notificationEmail, Boolean notificationFcm, Language language) {
        this.notificationemail = notificationEmail;
        this.notificationfcm = notificationFcm;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getNotificationemail() {
        return notificationemail;
    }

    public void setNotificationemail(Boolean notificationemail) {
        this.notificationemail = notificationemail;
    }

    public Boolean getNotificationfcm() {
        return notificationfcm;
    }

    public void setNotificationfcm(Boolean notificationfcm) {
        this.notificationfcm = notificationfcm;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public users getUser() {
        return user;
    }

    public void setUser(users user) {
        this.user = user;
    }
}
