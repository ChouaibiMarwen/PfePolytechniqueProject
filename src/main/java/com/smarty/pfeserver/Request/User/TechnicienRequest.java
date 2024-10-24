package com.smarty.pfeserver.Request.User;

import com.smarty.pfeserver.Enum.Project.SoftSkillsEnum;
import com.smarty.pfeserver.Enum.User.IdTypeEnum;
import com.smarty.pfeserver.Request.Tools.AddressRequest;

import java.util.HashSet;
import java.util.Set;

public class TechnicienRequest {
    private String email ;
    private String password;
    private String phonenumber;
    private String idnumber;
    private IdTypeEnum idType;
    private PersonalInformationRequest informationRequest;
    private AddressRequest useraddressRequest;
    private Set<SoftSkillsEnum> softSkillsEnumSet = new HashSet<>();

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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public IdTypeEnum getIdType() {
        return idType;
    }

    public void setIdType(IdTypeEnum idType) {
        this.idType = idType;
    }

    public PersonalInformationRequest getInformationRequest() {
        return informationRequest;
    }

    public void setInformationRequest(PersonalInformationRequest informationRequest) {
        this.informationRequest = informationRequest;
    }

    public AddressRequest getUseraddressRequest() {
        return useraddressRequest;
    }

    public void setUseraddressRequest(AddressRequest useraddressRequest) {
        this.useraddressRequest = useraddressRequest;
    }

    public Set<SoftSkillsEnum> getSoftSkillsEnumSet() {
        return softSkillsEnumSet;
    }

    public void setSoftSkillsEnumSet(Set<SoftSkillsEnum> softSkillsEnumSet) {
        this.softSkillsEnumSet = softSkillsEnumSet;
    }
}
