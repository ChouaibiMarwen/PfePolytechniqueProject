package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.LoanType;
import com.camelsoft.rayaserver.Enum.Project.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.WorkSector;
import com.camelsoft.rayaserver.Models.File.File_model;
import lombok.Data;

import java.util.Date;
@Data

public class LoanRequest {
    private Long id;
    private String englishfirstname;
    private String englishlastname;
    private String englishsecondname;
    private String englishthirdname;
    private String familyname;
    private String fathername;
    private String grandfathername;
    private Date birthdate;
    private String email;
    private String phonenumber;
    private String postcode;
    private String unitnumber;
    private String name;
    private Date retirementdate;
    private WorkSector sectortype = WorkSector.NONE;
    private String copynumber;
    private String additionalnumber;
    private String buildingnumber;
    private MaritalStatus maritalstatus = MaritalStatus.NONE;
    private String numberofdependents;
    private String nationalid;
    private String nationalidissuedate;
    private String nationalidexpirydate;
    private String city;
    private String district;
    private String primaryaddress;
    private String streetname;
    private String worksector;
    private Double salary;
    private String employername;
    private LoanType loantype = LoanType.NONE;
    private Date firstinstallment;
    private String purposeofloan;
    private String balloonloan;
    private Double loanamount;
    private Integer loanterm;
    private File_model attachment;
    private String carmark;
    private String carmodel;
    private String caryear;
    private String carvin;
    private String carcolor;
    private String carquantity;
    private String note;
}
