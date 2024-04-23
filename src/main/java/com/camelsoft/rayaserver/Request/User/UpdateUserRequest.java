package com.camelsoft.rayaserver.Request.User;

import lombok.Data;

@Data
public class UpdateUserRequest {


    private String firstname;
    private String lastName;
    private String phonenumber;
    private String country;
    private String city;
    private String sexe;
    private boolean notificationfcm=true;
    private boolean notificationemail=true;


}
