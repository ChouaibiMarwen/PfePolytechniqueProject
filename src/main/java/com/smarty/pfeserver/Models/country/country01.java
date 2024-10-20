package com.smarty.pfeserver.Models.country;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country01")
public class country01 implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "country_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "name_ar")
    private String name_ar;
    @Column(name = "name_fr")
    private String name_fr;
    @Column(name = "code")
    private String code;
    @Column(name = "currency")
    private String currency;

    @Column(name = "iso")
    private String iso;

    @ElementCollection
    @Column(name = "mask")
    private List<String> mask = new ArrayList<>();
    @Column(name = "currency_symbol")
    private String currency_symbol;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "country01_id")
    private List<Timezone> timezones=new ArrayList<>();
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "emoji")
    private String emoji;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "country01_id")
    private List<State> states = new ArrayList<>();

    public country01() {
    }

    public country01(String name, String name_ar, String name_fr, String code, String currency, String iso, List<String> mask, String currency_symbol, List<Timezone> timezones, String latitude, String longitude, String emoji, List<State> states) {
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.code = code;
        this.currency = currency;
        this.iso = iso;
        this.mask = mask;
        this.currency_symbol = currency_symbol;
        this.timezones = timezones;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emoji = emoji;
        this.states = states;
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

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public List<String> getMask() {
        return mask;
    }

    public void setMask(List<String> mask) {
        this.mask = mask;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public List<Timezone> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<Timezone> timezones) {
        this.timezones = timezones;
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

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
