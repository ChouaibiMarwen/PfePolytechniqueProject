package com.camelsoft.rayaserver.Request.User;

public class SignInRequest {

    private String username;
    private String companyName;
    private String password;
    private String deviceId;
    private String deviceType;
    private String ip;
    private String tokendevice;
    public SignInRequest() {
    }

    public SignInRequest(String username, String companyName, String password, String deviceId, String deviceType, String ip, String tokendevice) {
        this.username = username;
        this.companyName = companyName;
        this.password = password;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.ip = ip;
        this.tokendevice = tokendevice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTokendevice() {
        return tokendevice;
    }

    public void setTokendevice(String tokendevice) {
        this.tokendevice = tokendevice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
