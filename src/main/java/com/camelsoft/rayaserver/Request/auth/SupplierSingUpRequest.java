package com.camelsoft.rayaserver.Request.auth;

import com.camelsoft.rayaserver.Enum.User.IdTypeEnum;
import com.camelsoft.rayaserver.Request.Tools.AddressRequest;
import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;
import lombok.Data;

@Data
public class SupplierSingUpRequest {
    private String email ;
    private String password;
    private String phonenumber;
    private Long suppliernumber;
    private String companyName;
    private String idnumber;
    private IdTypeEnum idType;
    private PersonalInformationRequest informationRequest;
    private AddressRequest useraddressRequest;
    private AddressRequest billingaddressRequest;

    public SupplierSingUpRequest() {
    }
}
