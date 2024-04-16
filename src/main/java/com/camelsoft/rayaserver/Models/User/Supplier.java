package com.camelsoft.rayaserver.Models.User;


import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "supplier_number")
    private Long suppliernumber;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Loan> loans = new HashSet<>();
    @Transient
    private String city;

    @Transient
    private String name;

    @Transient
    private Long userId;

    @Transient
    private Double ratingAverage;

    @Transient
    private Integer ratingCount;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "supplier_files_id")
    private Set<File_model> images = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private users user;

    @Column(name = "timestmp")
    private Date timestmp;

    public Supplier() {
        this.timestmp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuppliernumber() {
        return suppliernumber;
    }

    public void setSuppliernumber(Long suppliernumber) {
        this.suppliernumber = suppliernumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }


    public Set<File_model> getImages() {
        return images;
    }

    public void setImages(Set<File_model> images) {
        this.images = images;
    }

    public users getUser() {
        return user;
    }

    public void setUser(users user) {
        this.user = user;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
