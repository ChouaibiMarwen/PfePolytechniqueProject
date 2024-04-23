package com.camelsoft.rayaserver.Request.auth;


import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;
import lombok.Data;

@Data
public class CustomerSingUpRequest {
    private String email ;
    private String password;
    private String phonenumber;
    private PersonalInformationRequest informationRequest;

    public CustomerSingUpRequest() {
    }
}
