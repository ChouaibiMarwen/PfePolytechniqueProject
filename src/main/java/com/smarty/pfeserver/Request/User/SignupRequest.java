package com.smarty.pfeserver.Request.User;

import com.smarty.pfeserver.Enum.User.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name = "";
    private String firstnameen;
    private String lastnameen;
    private String firstnamear;
    private String lastnamear;
    private Gender gender;
    private String phonenumber;
    private Date birthDate;
    private Long[] ids;

}
