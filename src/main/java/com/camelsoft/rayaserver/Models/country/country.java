package com.camelsoft.rayaserver.Models.country;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "country")
public class country implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;


    @Column(name = "administrative")
    private String administrative_area;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position")
    private positions position;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "timestmp")
    private Date timestmp;

    public country() {
        this.timestmp = new Date();
    }

    public country(String administrative, String address, Double prix) {
        this.administrative_area = administrative;
        this.address = address;
        this.prix = prix;
        this.timestmp = new Date();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public positions getPosition() {
        return position;
    }

    public void setPosition(positions position) {
        this.position = position;
    }


    public String getAdministrative_area() {
        return administrative_area;
    }

    public void setAdministrative_area(String administrative_area) {
        this.administrative_area = administrative_area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }


    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
