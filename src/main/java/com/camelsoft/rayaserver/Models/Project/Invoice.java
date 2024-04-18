package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Invoice")
public class Invoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "invoicenumber")
    private Integer invoicenumber = 0;
    @Column(name = "status")
    private InvoiceStatus status = InvoiceStatus.UNPAID;
    @Column(name = "invoicedate")
    private Date invoicedate = new Date();
    @Column(name = "duedate")
    private Date duedate = new Date();
    @Column(name = "currency")
    private String currency="SAR";
    @Column(name = "suppliername")
    private String suppliername;
    @Column(name = "supplierzipcode")
    private String supplierzipcode;
    @Column(name = "supplierstreetadress")
    private String supplierstreetadress;
    @Column(name = "supplierphonenumber")
    private String supplierphonenumber;
    @Column(name = "bankname")
    private String bankname;
    @Column(name = "bankzipcode")
    private String bankzipcode;
    @Column(name = "bankstreetadress")
    private String bankstreetadress;
    @Column(name = "bankphonenumber")
    private String bankphonenumber;
    @Column(name = "vehicleregistration")
    private String vehicleregistration;
    @Column(name = "vehiclecolor")
    private String vehiclecolor;
    @Column(name = "vehiclevin")
    private String vehiclevin;
    @Column(name = "vehiclemodel")
    private String vehiclemodel;
    @Column(name = "vehiclemark")
    private String vehiclemark;
    @Column(name = "vehiclemileage")
    private String vehiclemileage;
    @Column(name = "vehiclemotexpiry")
    private String vehiclemotexpiry;
    @Column(name = "vehicleenginesize")
    private String vehicleenginesize;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "invoice_products",
            joinColumns =
            @JoinColumn(name = "invoice_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Set<Product> products = new HashSet<>();
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(columnDefinition = "TEXT",name = "remark")
    private String remark;
   @Column(name = "related")
    private InvoiceRelated related = InvoiceRelated.NONE;
   @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id_createdby", nullable = false)
    private users createdby;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id_relatedto", nullable = false)
    private users relatedto;
    public Invoice() {
        this.timestamp = new Date();
    }

    public Invoice(Integer invoicenumber, Date invoicedate, Date duedate, String currency, String suppliername, String supplierzipcode, String supplierstreetadress, String supplierphonenumber, String bankname, String bankzipcode, String bankstreetadress, String bankphonenumber, String vehicleregistration, String vehiclecolor, String vehiclevin, String vehiclemodel, String vehiclemark, String vehiclemileage, String vehiclemotexpiry, String vehicleenginesize, Set<Product> products,users createdby,InvoiceRelated related,users relatedto) {
        this.invoicenumber = invoicenumber;
        this.invoicedate = invoicedate;
        this.duedate = duedate;
        this.currency = currency;
        this.suppliername = suppliername;
        this.supplierzipcode = supplierzipcode;
        this.supplierstreetadress = supplierstreetadress;
        this.supplierphonenumber = supplierphonenumber;
        this.bankname = bankname;
        this.bankzipcode = bankzipcode;
        this.bankstreetadress = bankstreetadress;
        this.bankphonenumber = bankphonenumber;
        this.vehicleregistration = vehicleregistration;
        this.vehiclecolor = vehiclecolor;
        this.vehiclevin = vehiclevin;
        this.vehiclemodel = vehiclemodel;
        this.vehiclemark = vehiclemark;
        this.vehiclemileage = vehiclemileage;
        this.vehiclemotexpiry = vehiclemotexpiry;
        this.vehicleenginesize = vehicleenginesize;
        this.products = products;
        this.createdby = createdby;
        this.relatedto = relatedto;
        this.related=related;
        this.timestamp = new Date();
    }

    public InvoiceRelated getRelated() {
        return related;
    }

    public void setRelated(InvoiceRelated related) {
        this.related = related;
    }

    public users getCreatedby() {
        return createdby;
    }

    public void setCreatedby(users createdby) {
        this.createdby = createdby;
    }

    public users getRelatedto() {
        return relatedto;
    }

    public void setRelatedto(users relatedto) {
        this.relatedto = relatedto;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(Integer invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Date getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(Date invoicedate) {
        this.invoicedate = invoicedate;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierzipcode() {
        return supplierzipcode;
    }

    public void setSupplierzipcode(String supplierzipcode) {
        this.supplierzipcode = supplierzipcode;
    }

    public String getSupplierstreetadress() {
        return supplierstreetadress;
    }

    public void setSupplierstreetadress(String supplierstreetadress) {
        this.supplierstreetadress = supplierstreetadress;
    }

    public String getSupplierphonenumber() {
        return supplierphonenumber;
    }

    public void setSupplierphonenumber(String supplierphonenumber) {
        this.supplierphonenumber = supplierphonenumber;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankzipcode() {
        return bankzipcode;
    }

    public void setBankzipcode(String bankzipcode) {
        this.bankzipcode = bankzipcode;
    }

    public String getBankstreetadress() {
        return bankstreetadress;
    }

    public void setBankstreetadress(String bankstreetadress) {
        this.bankstreetadress = bankstreetadress;
    }

    public String getBankphonenumber() {
        return bankphonenumber;
    }

    public void setBankphonenumber(String bankphonenumber) {
        this.bankphonenumber = bankphonenumber;
    }

    public String getVehicleregistration() {
        return vehicleregistration;
    }

    public void setVehicleregistration(String vehicleregistration) {
        this.vehicleregistration = vehicleregistration;
    }

    public String getVehiclecolor() {
        return vehiclecolor;
    }

    public void setVehiclecolor(String vehiclecolor) {
        this.vehiclecolor = vehiclecolor;
    }

    public String getVehiclevin() {
        return vehiclevin;
    }

    public void setVehiclevin(String vehiclevin) {
        this.vehiclevin = vehiclevin;
    }

    public String getVehiclemodel() {
        return vehiclemodel;
    }

    public void setVehiclemodel(String vehiclemodel) {
        this.vehiclemodel = vehiclemodel;
    }

    public String getVehiclemark() {
        return vehiclemark;
    }

    public void setVehiclemark(String vehiclemark) {
        this.vehiclemark = vehiclemark;
    }

    public String getVehiclemileage() {
        return vehiclemileage;
    }

    public void setVehiclemileage(String vehiclemileage) {
        this.vehiclemileage = vehiclemileage;
    }

    public String getVehiclemotexpiry() {
        return vehiclemotexpiry;
    }

    public void setVehiclemotexpiry(String vehiclemotexpiry) {
        this.vehiclemotexpiry = vehiclemotexpiry;
    }

    public String getVehicleenginesize() {
        return vehicleenginesize;
    }

    public void setVehicleenginesize(String vehicleenginesize) {
        this.vehicleenginesize = vehicleenginesize;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}