package com.camelsoft.rayaserver.Request.auth;


import com.camelsoft.rayaserver.Enum.User.IdTypeEnum;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Request.User.PersonalInformationRequest;
import lombok.Data;

@Data
public class CustomerSingUpRequest {
    private String email ;
    private String password;
    private String phonenumber;
    private Long  iddepartment;
    private Long  idroledepartment;
    private Long  idclassification;
    private Long suppliernumber;
    private String companyName;
    private String idnumber;
    private IdTypeEnum idType;
    private PersonalInformationRequest informationRequest;

    public CustomerSingUpRequest() {
    }
}
