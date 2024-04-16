package com.camelsoft.rayaserver.Models.country;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "countries")
public class Root implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "country_id" )
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "iso3")
    private String iso3;
    @Column(name = "iso2")
    private String iso2;
    @Column(name = "numeric_code")
    private String numeric_code;
    @Column(name = "phone_code")
    private String phone_code;
    @Column(name = "capital")
    private String capital;
    @Column(name = "currency")
    private String currency;
    @Column(name = "currency_symbol")
    private String currency_symbol;
    @Column(name = "tld")
    private String tld;
    @Column(name = "natives")
    private String natives;
    @Column(name = "region")
    private String region;
    @Column(name = "subregion")
    private String subregion;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinTable(name = "countries_timezones", joinColumns = @JoinColumn(name = "country_id"), inverseJoinColumns = @JoinColumn(name = "timezones_id"))
    private List<Timezone> timezones;
    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "translations_country", nullable = true, referencedColumnName = "translations_id")
    private Translations translations;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "emoji")
    private String emoji;
    @Column(name = "emojiU")
    private String emojiU;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinTable(name = "countries_states", joinColumns = @JoinColumn(name = "country_id"), inverseJoinColumns = @JoinColumn(name = "state_id"))
    private List<State> states;

    public Root() {
    }

    public Root(String name, String iso3, String iso2, String numeric_code, String phone_code, String capital, String currency, String currency_symbol, String tld, String natives, String region, String subregion, List<Timezone> timezones, Translations translations, String latitude, String longitude, String emoji, String emojiU, List<State> states) {
        this.name = name;
        this.iso3 = iso3;
        this.iso2 = iso2;
        this.numeric_code = numeric_code;
        this.phone_code = phone_code;
        this.capital = capital;
        this.currency = currency;
        this.currency_symbol = currency_symbol;
        this.tld = tld;
        this.natives = natives;
        this.region = region;
        this.subregion = subregion;
        this.timezones = timezones;
        this.translations = translations;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emoji = emoji;
        this.emojiU = emojiU;
        this.states = states;
    }

    public String getNatives() {
        return natives;
    }

    public void setNatives(String natives) {
        this.natives = natives;
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

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getNumeric_code() {
        return numeric_code;
    }

    public void setNumeric_code(String numeric_code) {
        this.numeric_code = numeric_code;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public List<Timezone> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<Timezone> timezones) {
        this.timezones = timezones;
    }

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
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

    public String getEmojiU() {
        return emojiU;
    }

    public void setEmojiU(String emojiU) {
        this.emojiU = emojiU;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
    public void addStates(State state) {
        this.states.add(state);
    }
}
