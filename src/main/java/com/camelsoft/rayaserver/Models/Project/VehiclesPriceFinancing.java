package com.camelsoft.rayaserver.Models.Project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "VehiclesPriceFinancing")
public class VehiclesPriceFinancing  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "price")
    private Double price = 0D;
    @Column(name = "currency")
    private String currency = "SAR";
    @Column(name = "discount")
    private Boolean discount =false;
    @Column(name = "discountpercentage")
    private Double discountpercentage = 0D;
    @Column(name = "discountamount")
    private Double discountamount = 0D;
    @Column(name = "vatpercentage")
    private Double vatpercentage = 0D;
    @Column(name = "vatamount")
    private Double vatamount = 0D;
    @Column(name = "totalamount")
    private Double totalamount = 0D;
    @Column(name = "timestamp")
    private Date timestamp;

    public VehiclesPriceFinancing() {
        this.timestamp=new Date();
    }

    public VehiclesPriceFinancing(Double price, String currency, Boolean discount, Double discountpercentage, Double discountamount, Double vatpercentage, Double vatamount, Double totalamount) {
        this.price = price;
        this.currency = currency;
        this.discount = discount;
        this.discountpercentage = discountpercentage;
        this.discountamount = discountamount;
        this.vatpercentage = vatpercentage;
        this.vatamount = vatamount;
        this.totalamount = totalamount;
        this.timestamp=new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public Double getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(Double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public Double getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(Double discountamount) {
        this.discountamount = discountamount;
    }

    public Double getVatpercentage() {
        return vatpercentage;
    }

    public void setVatpercentage(Double vatpercentage) {
        this.vatpercentage = vatpercentage;
    }

    public Double getVatamount() {
        return vatamount;
    }

    public void setVatamount(Double vatamount) {
        this.vatamount = vatamount;
    }

    public Double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
