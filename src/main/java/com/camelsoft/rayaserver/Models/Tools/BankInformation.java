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
@Table(name = "Address")
public class BankInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "bank_information_id")
    private Long id;
    @Column(name = "bank_name")
    private String bankname;
    @Column(name = "bank_address")
    private String bankaddress;
    @Column(name = "account_name")
    private String accountname;
    @Column(name = "iban")
    private String iban;
    @Column(name = "rip")
    private String rip;
    @Column(name = "swift")
    private String swift;
    @Column(name = "currency")
    private String currency = "SAR";
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id_owner_bank")
    private users user;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public BankInformation() {
    }
}
