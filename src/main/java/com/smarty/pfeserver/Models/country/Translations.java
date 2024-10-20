package com.smarty.pfeserver.Models.country;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Translations")
public class Translations implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "translations_id" )
    private Long id;
    @Column(name = "kr")
    public String kr;
    @Column(name = "br")
    public String br;
    @Column(name = "pt")
    public String pt;
    @Column(name = "nl")
    public String nl;
    @Column(name = "hr")
    public String hr;
    @Column(name = "fa")
    public String fa;
    @Column(name = "de")
    public String de;
    @Column(name = "es")
    public String es;
    @Column(name = "fr")
    public String fr;
    @Column(name = "ja")
    public String ja;
    @Column(name = "it")
    public String it;
    @Column(name = "cn")
    public String cn;

    public Translations() {
    }

    public Translations(String kr, String br, String pt, String nl, String hr, String fa, String de, String es, String fr, String ja, String it, String cn) {
        this.kr = kr;
        this.br = br;
        this.pt = pt;
        this.nl = nl;
        this.hr = hr;
        this.fa = fa;
        this.de = de;
        this.es = es;
        this.fr = fr;
        this.ja = ja;
        this.it = it;
        this.cn = cn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKr() {
        return kr;
    }

    public void setKr(String kr) {
        this.kr = kr;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
