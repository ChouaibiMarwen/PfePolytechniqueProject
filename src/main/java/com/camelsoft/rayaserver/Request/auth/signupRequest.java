package com.camelsoft.rayaserver.Request.auth;



import com.camelsoft.rayaserver.Enum.User.Gender;

import java.util.Date;

public class signupRequest {
    private String email;
    private String password;
    private String name = "";
    private Gender gender;
    private String phonenumber;
    private String country;
    private String city;
    private Date birthDate;
    private Long[] ids;

    public signupRequest() {
    }

    public signupRequest(String email, String password, String name, Gender gender, String phonenumber, String country, String city, Date birthDate, Long[] ids) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.country = country;
        this.city = city;
        this.birthDate = birthDate;
        this.ids = ids;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
