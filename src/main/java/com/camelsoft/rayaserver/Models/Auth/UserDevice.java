package com.camelsoft.rayaserver.Models.Auth;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserDevice")
public class UserDevice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id" )
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id")
    private users user;
    @Column(name = "deviceType" )
    private String deviceType;
    @Column(name = "device_Id" )
    private String deviceId;
    @Column(name = "ip" )
    private String ip;
    @Column(name = "token" )
    private String token;
   @Column(name = "tokendevice" )
    private String tokendevice;
    @Column(name = "timestmp")
    private Date timestmp;
    public UserDevice() {
        this.timestmp= new Date();
    }

    public UserDevice(users user, String deviceType, String deviceId, String ip, String token, String tokendevice) {
        this.user = user;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.tokendevice = tokendevice;
        this.ip = ip;
        this.token = token;
        this.timestmp= new Date();
    }

    public UserDevice(users user, String deviceType, String deviceId, String ip, String tokendevice) {

        this.user = user;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.tokendevice = tokendevice;
        this.ip = ip;
        this.timestmp= new Date();
    }

    public String getTokendevice() {
        return tokendevice;
    }

    public void setTokendevice(String tokendevice) {
        this.tokendevice = tokendevice;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
