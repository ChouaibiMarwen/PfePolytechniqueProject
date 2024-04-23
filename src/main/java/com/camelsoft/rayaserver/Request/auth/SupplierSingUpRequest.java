package com.camelsoft.rayaserver.Request.auth;

import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;
import lombok.Data;

@Data
public class SupplierSingUpRequest {
    private String email ;
    private String password;
    private String phonenumber;
    private Long suppliernumber;
    private PersonalInformationRequest informationRequest;

    public SupplierSingUpRequest() {
    }
}
