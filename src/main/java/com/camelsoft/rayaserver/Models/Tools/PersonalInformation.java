package com.camelsoft.rayaserver.Models.Tools;

import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "Personal_Information")
public class PersonalInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "personal_information_id")
    private Long id;
    @Column(name = "first_name_en")
    @NotEmpty(message = "*Please provide your english first name")
    private String firstnameen;
    @Column(name = "last_name_en")
    @NotEmpty(message = "*Please provide your english last name")
    private String lastnameen;
    @Column(name = "first_name_ar")
    @NotEmpty(message = "*Please provide your arabic first name")
    private String firstnamear;
    @Column(name = "last_name_ar")
    @NotEmpty(message = "*Please provide your arabic  last name")
    private String lastnamear;
    @Column(name = "birth_date")
    private Date birthDate=new Date();
    @Column(name = "second_name_ar")
    private String secondnamear;
    @Column(name = "third_name_ar")
    private String thirdnamear;
    @Column(name = "grand_father_name_ar")
    private String grandfathernamear;
    @Column(name = "second_name_en")
    private String secondnameen;
    @Column(name = "third_name_en")
    private String thirdnameen;
    @Column(name = "grandfather_name_en")
    private String grandfathernameen;
    @Column(name = "number_of_dependents")
    private Integer numberofdependents;
    @Column(name = "gender")
    private Gender gender = Gender.NONE;
    @Column(name = "worksector")
    private WorkSector worksector=WorkSector.NONE;
    @Column(name = "maritalstatus")
    private MaritalStatus maritalstatus = MaritalStatus.NONE;
    @Column(name = "timestmp")
    private Date timestmp = new Date();
    @OneToOne(mappedBy = "personalinformation")
    @JsonIgnore
    private users user;
    public PersonalInformation() {
    }
}
