package com.camelsoft.rayaserver.Models.User;

import com.camelsoft.rayaserver.Enum.Tools.Provider;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;

@Data
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
    @Transient
    private String name;
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "provider")
    private Provider provider = Provider.local;
    @Column(name = "suspend_reason")
    private String suspendraison = "NO RAISON FOUND";
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "profileimage")
    private File_model profileimage;
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "personal_id", referencedColumnName = "personal_information_id")
    private PersonalInformation personalinformation;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_id")
    private Set<File_model> documents = new HashSet<>();
    @OneToMany(mappedBy = "createdby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicescreated = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = { CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();
    @OneToMany(mappedBy = "relatedto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicesrecived = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BankInformation> bankinformations = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private BillingAddress billingAddress;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_privileges",
            joinColumns =
            @JoinColumn(name = "usar_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PurshaseOrder> purchaseOrders = new HashSet<>();

    @Column(name = "active")
    private Boolean active = true;
    @Column(name = "verified")
    private Boolean verified = null;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "otp")
    private String lastotp;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public users() {

    }

    public users(String username,String email, String password, String phonenumber, PersonalInformation personalinformation) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.personalinformation = personalinformation;
    }
    @PostLoad
    private void afterload(){
        if(this.personalinformation!=null){
            this.name = this.personalinformation.getFirstnameen()+" "+this.personalinformation.getLastnameen();
        }
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    @Override
    public int hashCode() {
        return 31; // Replace with any prime number
    }


}
