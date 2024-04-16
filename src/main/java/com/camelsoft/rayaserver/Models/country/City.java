package com.camelsoft.rayaserver.Models.country;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "City")
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "city_id" )
    private Long id;
    @Column(name = "city_name")
    private String name;
    @Column(name = "city_latitude")
    private String latitude;
    @Column(name = "city_longitude")
    private String longitude;
    @JsonIgnore
    @ManyToOne(targetEntity= State.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="state_id", nullable=true)
    private State state;
    public City() {
    }

    public City(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
