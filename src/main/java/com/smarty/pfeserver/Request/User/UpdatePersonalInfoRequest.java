package com.smarty.pfeserver.Request.User;

import com.smarty.pfeserver.Enum.User.IdTypeEnum;
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
