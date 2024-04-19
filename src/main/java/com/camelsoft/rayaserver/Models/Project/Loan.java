package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanType;
import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Loan_Request")
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "loan_id")
    private Long id;
    @Column(name = "englishfirstname")
    private String englishfirstname;
    @Column(name = "englishlastname")
    private String englishlastname;
    @Column(name = "englishsecondname")
    private String englishsecondname;
    @Column(name = "englishthirdname")
    private String englishthirdname;
    @Column(name = "familyname")
    private String familyname;
    @Column(name = "fathername")
    private String fathername;
    @Column(name = "grandfathername")
    private String grandfathername;
    @Column(name = "birthdate")
    private Date birthdate;
    @Column(name = "email")
    private String email;
    @Column(name = "phonenumber")

    private String phonenumber;
    @Column(name = "postcode")

    private String postcode;
    @Column(name = "unitnumber")

    private String unitnumber;
    @Column(name = "name")

    private String name;
    @Column(name = "retirementdate")

    private Date retirementdate;
    @Column(name = "sectortype")

    private WorkSector sectortype = WorkSector.NONE;
    @Column(name = "copynumber")

    private String copynumber;
    @Column(name = "additionalnumber")

    private String additionalnumber;
    @Column(name = "buildingnumber")

    private String buildingnumber;
    @Column(name = "maritalstatus")

    private MaritalStatus maritalstatus = MaritalStatus.NONE;
    @Column(name = "numberofdependents")

    private String numberofdependents;
    @Column(name = "nationalid")

    private String nationalid;
    @Column(name = "nationalidissuedate")

    private String nationalidissuedate;
    @Column(name = "nationalidexpirydate")

    private String nationalidexpirydate;
    @Column(name = "city")

    private String city;
    @Column(name = "district")

    private String district;
    @Column(name = "primaryaddress")

    private String primaryaddress;
    @Column(name = "streetname")

    private String streetname;
    @Column(name = "worksector")

    private String worksector;
    @Column(name = "salary")

    private Double salary;
    @Column(name = "employername")

    private String employername;
    @Column(name = "loantype")

    private LoanType loantype = LoanType.NONE;
    @Column(name = "firstinstallment")

    private Date firstinstallment;
    @Column(name = "purposeofloan")

    private String purposeofloan;
    @Column(name = "balloonloan")

    private String balloonloan;
    @Column(name = "loanamount")

    private Double loanamount;
    @Column(name = "loanterm")

    private Integer loanterm;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private File_model attachment;
    @Column(name = "carmark")

    private String carmark;
    @Column(name = "carmodel")
    private String carmodel;
    @Column(name = "caryear")
    private String caryear;
    @Column(name = "carvin")

    private String carvin;
    @Column(name = "carcolor")
    private String carcolor;
    @Column(name = "carquantity")
    private String carquantity;
    @Column(name = "currency")
    private String currency="SAR";
    @Column(columnDefinition = "TEXT",name = "note")
    private String note;
    @Column(name = "LoanStatus")
    private LoanStatus status = LoanStatus.WAITING;
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    @Column(name = "timestamp")
    private Date timestamp;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;

    public Loan(String englishfirstname, String englishlastname, String englishsecondname, String englishthirdname, String familyname, String fathername, String grandfathername, Date birthdate, String email, String phonenumber, String postcode, String unitnumber, String name, Date retirementdate, WorkSector sectortype, String copynumber, String additionalnumber, String buildingnumber, MaritalStatus maritalstatus, String numberofdependents, String nationalid, String nationalidissuedate, String nationalidexpirydate, String city, String district, String primaryaddress, String streetname, String worksector, Double salary, String employername, LoanType loantype, Date firstinstallment, String purposeofloan, String balloonloan, Double loanamount, Integer loanterm, File_model attachment, String carmark, String carmodel, String caryear, String carvin, String carcolor, String carquantity, String note, Supplier supplier,String currency) {
        this.englishfirstname = englishfirstname;
        this.englishlastname = englishlastname;
        this.englishsecondname = englishsecondname;
        this.englishthirdname = englishthirdname;
        this.familyname = familyname;
        this.fathername = fathername;
        this.grandfathername = grandfathername;
        this.birthdate = birthdate;
        this.email = email;
        this.phonenumber = phonenumber;
        this.postcode = postcode;
        this.unitnumber = unitnumber;
        this.name = name;
        this.retirementdate = retirementdate;
        this.sectortype = sectortype;
        this.copynumber = copynumber;
        this.additionalnumber = additionalnumber;
        this.buildingnumber = buildingnumber;
        this.maritalstatus = maritalstatus;
        this.numberofdependents = numberofdependents;
        this.nationalid = nationalid;
        this.nationalidissuedate = nationalidissuedate;
        this.nationalidexpirydate = nationalidexpirydate;
        this.city = city;
        this.district = district;
        this.primaryaddress = primaryaddress;
        this.streetname = streetname;
        this.worksector = worksector;
        this.salary = salary;
        this.employername = employername;
        this.loantype = loantype;
        this.firstinstallment = firstinstallment;
        this.purposeofloan = purposeofloan;
        this.balloonloan = balloonloan;
        this.loanamount = loanamount;
        this.loanterm = loanterm;
        this.attachment = attachment;
        this.carmark = carmark;
        this.carmodel = carmodel;
        this.caryear = caryear;
        this.carvin = carvin;
        this.carcolor = carcolor;
        this.carquantity = carquantity;
        this.note = note;
        this.supplier = supplier;
        this.currency = currency;
        this.timestamp = new Date();
    }

    public String getCurrency() {
        return currency;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Loan() {
        this.timestamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglishfirstname() {
        return englishfirstname;
    }

    public void setEnglishfirstname(String englishfirstname) {
        this.englishfirstname = englishfirstname;
    }

    public String getEnglishlastname() {
        return englishlastname;
    }

    public void setEnglishlastname(String englishlastname) {
        this.englishlastname = englishlastname;
    }

    public String getEnglishsecondname() {
        return englishsecondname;
    }

    public void setEnglishsecondname(String englishsecondname) {
        this.englishsecondname = englishsecondname;
    }

    public String getEnglishthirdname() {
        return englishthirdname;
    }

    public void setEnglishthirdname(String englishthirdname) {
        this.englishthirdname = englishthirdname;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getGrandfathername() {
        return grandfathername;
    }

    public void setGrandfathername(String grandfathername) {
        this.grandfathername = grandfathername;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getUnitnumber() {
        return unitnumber;
    }

    public void setUnitnumber(String unitnumber) {
        this.unitnumber = unitnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRetirementdate() {
        return retirementdate;
    }

    public void setRetirementdate(Date retirementdate) {
        this.retirementdate = retirementdate;
    }

    public WorkSector getSectortype() {
        return sectortype;
    }

    public void setSectortype(WorkSector sectortype) {
        this.sectortype = sectortype;
    }

    public String getCopynumber() {
        return copynumber;
    }

    public void setCopynumber(String copynumber) {
        this.copynumber = copynumber;
    }

    public String getAdditionalnumber() {
        return additionalnumber;
    }

    public void setAdditionalnumber(String additionalnumber) {
        this.additionalnumber = additionalnumber;
    }

    public String getBuildingnumber() {
        return buildingnumber;
    }

    public void setBuildingnumber(String buildingnumber) {
        this.buildingnumber = buildingnumber;
    }

    public MaritalStatus getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(MaritalStatus maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getNumberofdependents() {
        return numberofdependents;
    }

    public void setNumberofdependents(String numberofdependents) {
        this.numberofdependents = numberofdependents;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getNationalidissuedate() {
        return nationalidissuedate;
    }

    public void setNationalidissuedate(String nationalidissuedate) {
        this.nationalidissuedate = nationalidissuedate;
    }

    public String getNationalidexpirydate() {
        return nationalidexpirydate;
    }

    public void setNationalidexpirydate(String nationalidexpirydate) {
        this.nationalidexpirydate = nationalidexpirydate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrimaryaddress() {
        return primaryaddress;
    }

    public void setPrimaryaddress(String primaryaddress) {
        this.primaryaddress = primaryaddress;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getWorksector() {
        return worksector;
    }

    public void setWorksector(String worksector) {
        this.worksector = worksector;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmployername() {
        return employername;
    }

    public void setEmployername(String employername) {
        this.employername = employername;
    }

    public LoanType getLoantype() {
        return loantype;
    }

    public void setLoantype(LoanType loantype) {
        this.loantype = loantype;
    }

    public Date getFirstinstallment() {
        return firstinstallment;
    }

    public void setFirstinstallment(Date firstinstallment) {
        this.firstinstallment = firstinstallment;
    }

    public String getPurposeofloan() {
        return purposeofloan;
    }

    public void setPurposeofloan(String purposeofloan) {
        this.purposeofloan = purposeofloan;
    }

    public String getBalloonloan() {
        return balloonloan;
    }

    public void setBalloonloan(String balloonloan) {
        this.balloonloan = balloonloan;
    }

    public Double getLoanamount() {
        return loanamount;
    }

    public void setLoanamount(Double loanamount) {
        this.loanamount = loanamount;
    }

    public Integer getLoanterm() {
        return loanterm;
    }

    public void setLoanterm(Integer loanterm) {
        this.loanterm = loanterm;
    }

    public File_model getAttachment() {
        return attachment;
    }

    public void setAttachment(File_model attachment) {
        this.attachment = attachment;
    }

    public String getCarmark() {
        return carmark;
    }

    public void setCarmark(String carmark) {
        this.carmark = carmark;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getCaryear() {
        return caryear;
    }

    public void setCaryear(String caryear) {
        this.caryear = caryear;
    }

    public String getCarvin() {
        return carvin;
    }

    public void setCarvin(String carvin) {
        this.carvin = carvin;
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor;
    }

    public String getCarquantity() {
        return carquantity;
    }

    public void setCarquantity(String carquantity) {
        this.carquantity = carquantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
