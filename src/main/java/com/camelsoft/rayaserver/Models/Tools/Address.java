package com.camelsoft.rayaserver.Models.Tools;

import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.State;
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
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "address_id")
    private Long id;
    @Column(name = "addressline1")
    private String addressline1;
    @Column(name = "addressline2")
    private String addressline2;
    @Column(name = "postcode")
    private String postcode;
    @Column(name = "building")
    private String building;
   @Column(name = "unitnumber")
    private String unitnumber;
    @Column(name = "streetname")
    private String streetname;
    @Column(name = "primaryaddress")
    private Boolean primaryaddress;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Root country;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State city;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id_owner_address")
    private users user;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public Address() {

    }


   /* public void add(userTask task) {
        usersTasks.add(task);
        task.setTasks(this);
    }*/

}
