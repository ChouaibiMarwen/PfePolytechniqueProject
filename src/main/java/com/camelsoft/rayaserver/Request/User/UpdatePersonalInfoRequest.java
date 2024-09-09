package com.camelsoft.rayaserver.Request.User;

import com.camelsoft.rayaserver.Enum.User.IdTypeEnum;
import lombok.Data;

@Data
public class UpdatePersonalInfoRequest {
    private String phonenumber;
    private Long  iddepartment;
    private Long  idroledepartment;
    private Long  idclassification;
    private Long suppliernumber;
    private String companyName;
    private String idnumber;
    private IdTypeEnum idType;
    private PersonalInformationRequest informationRequest;
    private String vatNumber;

    public UpdatePersonalInfoRequest() {
    }
}
