package com.camelsoft.rayaserver.Models.User;


import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.Tools.Provider;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users_table",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_name"),
                @UniqueConstraint(columnNames = "phone_number"),
                @UniqueConstraint(columnNames = "email")
        })
public class users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    @Length(min = 5, message = "*Your user name must have at least 5 characters")
    @NotEmpty(message = "*Please provide a user name")
    private String username;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @JsonIgnore
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "name")
    private String name = "";
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "office_phone")
    private String officePhone;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "providerid")
    private String providerId = "";
    @Column(name = "provider")
    private Provider provider = Provider.local;
    @Column(name = "suspend_reason")
    private String suspendReason;
    @Column(name = "active")
    private Boolean active = true;
    @Column(name = "address")
    private String address;
    @Column(name = "focalpointname")
    private String focalpointname;
    @Column(name = "registrationnumber")
    private String registrationnumber;
    @Column(name = "region")
    private String region;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "verified_docs")
    private Boolean verifiedDocs = false;
    @Column(name = "birth_date")
    private Date birthDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_privileges",
            joinColumns =
            @JoinColumn(name = "usar_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Supplier supplier;
    @Column(name = "notification_email")
    private Boolean notificationEmail = false;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "notification_fcm")
    private Boolean notificationFcm = false;
    @Column(name = "total_time")
    private Double totalTime = 0d;
    @Column(name = "first_name")
    @NotEmpty(message = "*Please provide your first name")
    private String firstname;
    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastname;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private File_model image;
    @Column(name = "otp")
    private String lastotp;

    @Column(name = "language")
    private String language;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_id")
    private Set<File_model> files = new HashSet<>();
    @OneToMany(mappedBy = "createdby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicescreated = new HashSet<>();
    @OneToMany(mappedBy = "relatedto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicesrecived = new HashSet<>();
    @Column(name = "timestmp")
    private Date timestmp;

    public users() {
        this.timestmp = new Date();
    }

    public users(String username, String phonenumber, String lastotp) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.lastotp = lastotp;
        this.timestmp = new Date();
    }

    public users(String username, String email, String password, String name, Gender gender, String phonenumber, String country) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.country = country;
        this.timestmp = new Date();
    }

    public users(String username, String email, String password, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.timestmp = new Date();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Invoice> getInvoicescreated() {
        return invoicescreated;
    }

    public void setInvoicescreated(Set<Invoice> invoicescreated) {
        this.invoicescreated = invoicescreated;
    }

    public Set<Invoice> getInvoicesrecived() {
        return invoicesrecived;
    }

    public void setInvoicesrecived(Set<Invoice> invoicesrecived) {
        this.invoicesrecived = invoicesrecived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getSuspendReason() {
        return suspendReason;
    }

    public void setSuspendReason(String suspendReason) {
        this.suspendReason = suspendReason;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFocalpointname() {
        return focalpointname;
    }

    public void setFocalpointname(String focalpointname) {
        this.focalpointname = focalpointname;
    }

    public String getRegistrationnumber() {
        return registrationnumber;
    }

    public void setRegistrationnumber(String registrationnumber) {
        this.registrationnumber = registrationnumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getVerifiedDocs() {
        return verifiedDocs;
    }

    public void setVerifiedDocs(Boolean verifiedDocs) {
        this.verifiedDocs = verifiedDocs;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Boolean getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(Boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getNotificationFcm() {
        return notificationFcm;
    }

    public void setNotificationFcm(Boolean notificationFcm) {
        this.notificationFcm = notificationFcm;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public File_model getImage() {
        return image;
    }

    public void setImage(File_model image) {
        this.image = image;
    }

    public String getLastotp() {
        return lastotp;
    }

    public void setLastotp(String lastotp) {
        this.lastotp = lastotp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<File_model> getFiles() {
        return files;
    }

    public void setFiles(Set<File_model> files) {
        this.files = files;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
