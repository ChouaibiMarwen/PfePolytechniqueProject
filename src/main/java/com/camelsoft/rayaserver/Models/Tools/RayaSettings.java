package com.camelsoft.rayaserver.Models.Tools;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "raya_settings")
public class RayaSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "allow_signup", nullable = false)
    private Boolean allowsignup = true;
    @Column(name = "allow_reset_password", nullable = false)
    private Boolean allowresetpassword = true;
    @Column(name = "timestamp")
    private Date timestamp=new Date();


    public RayaSettings() {
    }

    public RayaSettings(Boolean allowsignup, Boolean allowresetpassword) {
        this.allowsignup = allowsignup;
        this.allowresetpassword = allowresetpassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAllowsignup() {
        return allowsignup;
    }

    public void setAllowsignup(Boolean allowsignup) {
        this.allowsignup = allowsignup;
    }

    public Boolean getAllowresetpassword() {
        return allowresetpassword;
    }

    public void setAllowresetpassword(Boolean allowresetpassword) {
        this.allowresetpassword = allowresetpassword;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
