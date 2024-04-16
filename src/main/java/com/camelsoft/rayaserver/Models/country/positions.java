package com.camelsoft.rayaserver.Models.country;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "position")
public class positions implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "altitude")
    private  Double altitude;
    @Column(name = "timestmp")
    private Date timestmp;

    public positions() {
        this.timestmp = new Date();
    }

    public positions(Double longitude, Double laltitude) {
        this.longitude = longitude;
        this.altitude = laltitude;
        this.timestmp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
