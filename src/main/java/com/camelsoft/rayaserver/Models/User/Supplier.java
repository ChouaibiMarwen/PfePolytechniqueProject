package com.camelsoft.rayaserver.Models.User;


import com.camelsoft.rayaserver.Enum.User.IdTypeEnum;
import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "supplier", uniqueConstraints = {
        @UniqueConstraint(columnNames = "supplier_number"),

})
public class Supplier implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "supplier_number")
    private Long suppliernumber;

    @Column(name = "typeid")
    private IdTypeEnum idtype=IdTypeEnum.NONE;
    @Column(name = "id_number")
    private String idnumber;
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

    @OneToMany(fetch = FetchType.EAGER, cascade =  CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "images_media")  // Change to "images" if that's the actual column name
    private Set<MediaModel> images = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToOne(mappedBy = "supplier",fetch = FetchType.EAGER)
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
    private void afterLoad() {
        logger.debug("Entering @PostLoad method for Supplier with ID: {}", id);

        if (user != null) {
            logger.debug("User is not null, initializing personal information for user with ID: {}", user.getId());
            Hibernate.initialize(user.getPersonalinformation());
            if (user.getPersonalinformation() != null) {
                name = user.getPersonalinformation().getFirstnameen() + " " + user.getPersonalinformation().getLastnameen();
                userId = user.getId();
                logger.debug("Set name: {} and userId: {}", name, userId);
            } else {
                logger.debug("User personal information is null");
            }
        } else {
            logger.debug("User is null");
        }

        if (this.vehicles != null) {
            logger.debug("Vehicles are not null, initializing vehicles collection");
            Hibernate.initialize(this.vehicles);
            availableVehiclesCountBySupplier = 0;
            for (Vehicles vehicle : vehicles) {
                availableVehiclesCountBySupplier += vehicle.getStock();
            }
            logger.debug("Set availableVehiclesCountBySupplier: {}", availableVehiclesCountBySupplier);
        } else {
            availableVehiclesCountBySupplier = 0;
            logger.debug("Vehicles are null, set availableVehiclesCountBySupplier to 0");
        }

        // Add debug logs for ratingAverage and ratingCount if you plan to calculate them here
    }


    public Supplier() {
    }

    public Integer getAvailableVehiclesCountBySupplier() {
        return availableVehiclesCountBySupplier;
    }

    public IdTypeEnum getIdtype() {
        return idtype;
    }

    public void setIdtype(IdTypeEnum idtype) {
        this.idtype = idtype;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public void setAvailableVehiclesCountBySupplier(Integer availableVehiclesCountBySupplier) {
        this.availableVehiclesCountBySupplier = availableVehiclesCountBySupplier;
    }

    public Set<Service_Agreement> getServiceagreements() {
        return serviceagreements;
    }

    public void setServiceagreements(Set<Service_Agreement> serviceagreements) {
        this.serviceagreements = serviceagreements;
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

    public Set<MediaModel> getImages() {
        return images;
    }

    public void setImages(Set<MediaModel> images) {
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
