package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Vehicles.BodyStyle;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.FuelType;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.VehiclesPostStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Vehicles")
public class Vehicles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "carmodel")
    private String carmodel;
    @Column(name = "color")
    private String color;
    @Transient
    private Double price;
    @Column(name = "carvin")

    private String carvin;
    @Column(name = "enginesize")

    private Double enginesize=0D;
    @Column(name = "fueltype")

    private FuelType fueltype = FuelType.NONE;
    @Column(name = "bodystyle")

    private BodyStyle bodystyle = BodyStyle.NONE;
   @Column(name = "post_status")
    private VehiclesPostStatus status = VehiclesPostStatus.DRAFT;
   @Column(name = "stock")
    private Integer stock = 0;
    @Column(name = "exteriorfeatures")
    @ElementCollection
    private Set<String> exteriorfeatures = new HashSet<>();
    @Column(name = "interiorfeatures")
    @ElementCollection
    private Set<String> interiorfeatures = new HashSet<>();
    @Column(columnDefinition = "TEXT",name = "description")
    private String description;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "carimages_id")
    private VehiclesMedia carimages;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "VehiclesPriceFinancing_id")
    private VehiclesPriceFinancing Vehiclespricefinancing;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id",nullable = false)
    private Supplier supplier;
    @Column(name = "timestamp")
    private Date timestamp;

    public Vehicles() {
        this.timestamp = new Date();
    }

    public Vehicles(String carmodel, String color, String carvin, Double enginesize, FuelType fueltype, BodyStyle bodystyle, Set<String> exteriorfeatures, Set<String> interiorfeatures, String description, Integer stock,Supplier supplier) {
        this.carmodel = carmodel;
        this.color = color;
        this.carvin = carvin;
        this.enginesize = enginesize;
        this.fueltype = fueltype;
        this.bodystyle = bodystyle;
        this.exteriorfeatures = exteriorfeatures;
        this.interiorfeatures = interiorfeatures;
        this.description = description;
        this.stock=stock;
        this.supplier=supplier;
        this.timestamp=new Date();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCarvin() {
        return carvin;
    }

    public void setCarvin(String carvin) {
        this.carvin = carvin;
    }

    public Double getEnginesize() {
        return enginesize;
    }

    public void setEnginesize(Double enginesize) {
        this.enginesize = enginesize;
    }

    public FuelType getFueltype() {
        return fueltype;
    }

    public void setFueltype(FuelType fueltype) {
        this.fueltype = fueltype;
    }

    public BodyStyle getBodystyle() {
        return bodystyle;
    }

    public void setBodystyle(BodyStyle bodystyle) {
        this.bodystyle = bodystyle;
    }

    public Set<String> getExteriorfeatures() {
        return exteriorfeatures;
    }

    public void setExteriorfeatures(Set<String> exteriorfeatures) {
        this.exteriorfeatures = exteriorfeatures;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehiclesMedia getCarimages() {
        return carimages;
    }

    public void setCarimages(VehiclesMedia carimages) {
        this.carimages = carimages;
    }

    public VehiclesPriceFinancing getVehiclespricefinancing() {
        return Vehiclespricefinancing;
    }

    public void setVehiclespricefinancing(VehiclesPriceFinancing vehiclespricefinancing) {
        Vehiclespricefinancing = vehiclespricefinancing;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public VehiclesPostStatus getStatus() {
        return status;
    }

    public void setStatus(VehiclesPostStatus status) {
        this.status = status;
    }


    public Set<String> getInteriorfeatures() {
        return interiorfeatures;
    }

    public void setInteriorfeatures(Set<String> interiorfeatures) {
        this.interiorfeatures = interiorfeatures;
    }


}
