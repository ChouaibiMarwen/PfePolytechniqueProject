package com.camelsoft.rayaserver.Models.Tools;

import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "BillingAddress")
public class BillingAddress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "billing_address_id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "billingaddress")
    private String billingaddress;
    @Column(name = "country")
    private String country;
    @Column(name = "zipcode")
    private String zipcode;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "phonenumber")
    private String phonenumber;
    @OneToOne(mappedBy = "billingAddress", fetch = FetchType.LAZY)
    @JsonIgnore
    private users user;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public BillingAddress() {
    }
}
