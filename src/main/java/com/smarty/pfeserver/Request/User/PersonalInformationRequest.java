package com.smarty.pfeserver.Request.User;

import lombok.Data;

import java.util.Date;

@Data
public class PersonalInformationRequest {

    private String firstnameen;
    private String lastnameen;
    private String firstnamear;
    private String lastnamear;
    private Date birthDate=new Date();
    private String secondnamear;
    private String thirdnamear;
    private String grandfathernamear;
    private String secondnameen;
    private String thirdnameen;
    private String grandfathernameen;
    private Integer numberofdependents;
    private String gender;
    private String phonenumber;
    private String worksector;
    private String vatNumber;
}



