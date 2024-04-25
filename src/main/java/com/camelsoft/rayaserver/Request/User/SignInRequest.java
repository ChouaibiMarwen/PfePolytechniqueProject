package com.camelsoft.rayaserver.Request.User;

import lombok.Data;

@Data
public class SignInRequest {

    private String username;

    private String password;
    private String deviceId;
    private String deviceType;
    private String ip;
    private String tokendevice;
    public SignInRequest() {
    }


}
