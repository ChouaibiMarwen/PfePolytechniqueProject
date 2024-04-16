package com.camelsoft.rayaserver.Response.Auth;

import java.util.Date;

public class JwtResponse {
    private String token;
    private String refreshtoken;
    private String roles;
    private String deviceId;
    private String deviceType;
    private String ip;
    private Date expairytokendate;



    public JwtResponse(String token, String refreshtoken, String roles, String deviceId, String deviceType, String ip, Date expairytokendate) {
        this.token = token;
        this.refreshtoken = refreshtoken;
        this.roles = roles;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.ip = ip;
        this.expairytokendate = expairytokendate;
    }

    public JwtResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
