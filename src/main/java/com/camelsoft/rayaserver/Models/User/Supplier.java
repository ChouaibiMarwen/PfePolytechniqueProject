package com.camelsoft.rayaserver.Models.User;


import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
    @OneToOne(mappedBy = "supplier")
    @JsonIgnore
    private users user;

    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Vehicles> vehicles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PurshaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Service_Agreement> serviceagreements = new HashSet<>();

    @Transient
    private Integer availableVehiclesCountBySupplier = 0;

    @PostLoad
    private void afterload(){
        if(this.user!=null){
            if(this.user.getPersonalinformation()!=null){
                this.name = this.user.getPersonalinformation().getFirstnameen()+" "+this.user.getPersonalinformation().getLastnameen();
                this.userId = this.getUser().getId();
            }
        }
        if(this.vehicles!=null){
            for (Vehicles vehicles1 :vehicles) {
                this.availableVehiclesCountBySupplier+= vehicles1.getStock();
            }
        }else{
            this.availableVehiclesCountBySupplier = 0;
        }

    }

    public Integer getAvailableVehiclesCountBySupplier() {
        return availableVehiclesCountBySupplier;
    }

    public void setAvailableVehiclesCountBySupplier(Integer availableVehiclesCountBySupplier) {
        this.availableVehiclesCountBySupplier = availableVehiclesCountBySupplier;
    }

    public Set<PurshaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurshaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<Vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public Set<File_model> getImages() {
        return images;
    }

    public void setImages(Set<File_model> images) {
        this.images = images;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public Long getSuppliernumber() {
        return suppliernumber;
    }

    public void setSuppliernumber(Long suppliernumber) {
        this.suppliernumber = suppliernumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
