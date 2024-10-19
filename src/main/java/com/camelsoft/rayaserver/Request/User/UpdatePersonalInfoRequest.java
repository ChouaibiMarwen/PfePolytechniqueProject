package com.camelsoft.rayaserver.Request.User;

import com.camelsoft.rayaserver.Enum.User.IdTypeEnum;
import lombok.Data;

@Data
public class UpdatePersonalInfoRequest {
    private String phonenumber;
    private String idnumber;
    private IdTypeEnum idType;
    private PersonalInformationRequest informationRequest;

    public UpdatePersonalInfoRequest() {
    }
}
