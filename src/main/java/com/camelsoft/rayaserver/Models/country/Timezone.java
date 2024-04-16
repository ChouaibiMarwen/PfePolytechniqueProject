package com.camelsoft.rayaserver.Models.country;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Timezone")
public class Timezone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "timezone_id" )
    private Long id;
    @Column(name = "timezone_name")
    private String zonename;
    @Column(name = "timezone_gmt")
    private int gmtOffset;
    @Column(name = "timezone_gmt_offset_name")
    private String gmtOffsetName;
    @Column(name = "timezone_abbrivation")
    private String abbreviation;
    @Column(name = "timezone_tzname")
    private String tzName;

    @JsonIgnore
    @ManyToOne(targetEntity= Root.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="country_root", nullable=true)
    private Root root;

    public Timezone() {
    }

    public Timezone(String zonename, int gmtOffset, String gmtOffsetName, String abbreviation, String tzName) {
        this.zonename = zonename;
        this.gmtOffset = gmtOffset;
        this.gmtOffsetName = gmtOffsetName;
        this.abbreviation = abbreviation;
        this.tzName = tzName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneName() {
        return zonename;
    }

    public void setZoneName(String zonename) {
        this.zonename = zonename;
    }

    public int getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(int gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    public String getGmtOffsetName() {
        return gmtOffsetName;
    }

    public void setGmtOffsetName(String gmtOffsetName) {
        this.gmtOffsetName = gmtOffsetName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTzName() {
        return tzName;
    }

    public void setTzName(String tzName) {
        this.tzName = tzName;
    }
}
