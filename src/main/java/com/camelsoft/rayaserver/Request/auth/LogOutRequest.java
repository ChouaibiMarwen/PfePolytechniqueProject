package com.camelsoft.rayaserver.Request.auth;

public class LogOutRequest {

    private DeviceInfo deviceInfo;
    private String token;

    public LogOutRequest() {
    }

    public LogOutRequest(DeviceInfo deviceInfo, String token) {
        this.deviceInfo = deviceInfo;
        this.token = token;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
