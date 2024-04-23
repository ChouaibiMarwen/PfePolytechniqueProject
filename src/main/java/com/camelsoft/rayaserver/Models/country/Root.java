package com.camelsoft.rayaserver.Models.country;

import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.IdentificationInformation;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
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
    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private Set<Address> addresses = new HashSet<>();
     @JsonIgnore
    @OneToMany(mappedBy = "issuingcountry")
    private Set<IdentificationInformation> identificationInformations = new HashSet<>();
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


}
