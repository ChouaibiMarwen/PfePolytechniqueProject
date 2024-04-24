package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.User.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private Double quantity = 0D;
    @Column(name = "unitprice")
    private Double unitprice = 0D;
    @Column(name = "taxespercentage")
    private Double taxespercentage = 0D;
    @Column(name = "discountpercentage")
    private Double discountpercentage = 0D;
    @Column(name = "subtotal")
    private Double subtotal = 0D;
    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Invoice> invoice;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "supplier_id_product", nullable = false)
    private Supplier supplier;

    public Product() {

    }

    public Product(String name, Double quantity, Double unitprice, Double taxespercentage, Double discountpercentage, Double subtotal) {
        this.name = name;
        this.quantity = quantity;
        this.unitprice = unitprice;
        this.taxespercentage = taxespercentage;
        this.discountpercentage = discountpercentage;
        this.subtotal = subtotal;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Double getTaxespercentage() {
        return taxespercentage;
    }

    public void setTaxespercentage(Double taxespercentage) {
        this.taxespercentage = taxespercentage;
    }

    public Double getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(Double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(Set<Invoice> invoice) {
        this.invoice = invoice;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }
}
