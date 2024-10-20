package com.smarty.pfeserver.Models.Project;

import com.smarty.pfeserver.Enum.Project.Invoice.InvoiceRelated;
import com.smarty.pfeserver.Enum.Project.Invoice.InvoiceStatus;
import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.User.users;
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
    private InvoiceStatus status = InvoiceStatus.PENDING;
    @Column(name = "invoicedate")
    private Date invoicedate = new Date();
    @Column(name = "duedate")
    private Date duedate = new Date();
    @Column(name = "currency")
    private String currency = "SAR";
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
     @Column(name = "thirdpartypoid")
    private String thirdpartypoid;
    @Column(name = "bankzipcode")
    private String bankzipcode;
    @Column(name = "bankstreetadress")
    private String bankstreetadress;
    @Column(name = "bankphonenumber")
    private String bankphonenumber;
    @Column(name = "bankiban")
    private String bankiban;
    @Column(name = "bankrib")
    private String bankrib;
    @Column(name = "vehicleregistration")
    private String vehicleregistration;
    @Column(name = "vehiclecolor")
    private String vehiclecolor;
    @Column(name = "vehiclevin")
    private String vehiclevin;
    @Column(name = "vehiclediscountpercentage")
    private Double vehiclediscountpercentage = 0D;
    @Column(name = "vehiclediscountamount")
    private Double vehiclediscountamount = 0D;
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
    @Column(name = "vehicleprice")
    private Double vehicleprice = 0.0;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(columnDefinition = "TEXT", name = "remark")
    private String remark;
    @Column(name = "related")
    private InvoiceRelated related = InvoiceRelated.NONE;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id_createdby", nullable = false)
    private users createdby;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id_relatedto", nullable = false)
    private users relatedto;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id_confirmedby")
    private users confirmedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();
    @Transient
    private Long poid;
    @Transient
    private Double amountpaid = 0.0;
    @Transient
    private Boolean paid = false;
    @Transient
    private Double total = 0.0;
    @Transient
    private Long confirmedById;
    @Transient
    private Long suppliernumber;


    @Column(columnDefinition = "TEXT",name = "rejection_reason")
    private String rejectionreason;

    @Transient
    private String supplierVatnumber ;
    @Transient
    private RoleEnum rolecratedby ;

    public Invoice() {
        this.timestamp = new Date();
    }

    public Invoice(Integer invoicenumber, Date invoicedate, Date duedate, String currency, String suppliername, String supplierzipcode, String supplierstreetadress, String supplierphonenumber, String bankname, String bankzipcode, String bankstreetadress, String bankphonenumber, String vehicleregistration, String vehiclecolor, String vehiclevin, String vehiclemodel, String vehiclemark, String vehiclemileage, String vehiclemotexpiry, String vehicleenginesize, users createdby, InvoiceRelated related, users relatedto, String bankiban, String bankrib) {
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
        this.createdby = createdby;
        this.relatedto = relatedto;
        this.related = related;
        this.bankiban = bankiban;
        this.bankrib = bankrib;
        this.timestamp = new Date();
    }

    @PostLoad
    private void afterload() {
        this.total=0d;


        if (payments != null) {
            for (Payment p : payments) {
                this.amountpaid += p.getAmount();
            }
        }

        if(vehicleprice!=null)
            this.total+= vehicleprice;
        if(this.amountpaid!=null && this.total!=null && this.total>=this.amountpaid)
            this.paid = true;

        if(this.confirmedBy != null ){
            this.confirmedById = this.confirmedBy.getId();
        }else{
            this.confirmedById = null;
        }
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

    public String getThirdpartypoid() {
        return thirdpartypoid;
    }

    public void setThirdpartypoid(String thirdpartypoid) {
        this.thirdpartypoid = thirdpartypoid;
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

    public String getBankiban() {
        return bankiban;
    }

    public void setBankiban(String bankiban) {
        this.bankiban = bankiban;
    }

    public String getBankrib() {
        return bankrib;
    }

    public void setBankrib(String bankrib) {
        this.bankrib = bankrib;
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

    public Double getVehiclediscountpercentage() {
        return vehiclediscountpercentage;
    }

    public void setVehiclediscountpercentage(Double vehiclediscountpercentage) {
        this.vehiclediscountpercentage = vehiclediscountpercentage;
    }

    public Double getVehiclediscountamount() {
        return vehiclediscountamount;
    }

    public void setVehiclediscountamount(Double vehiclediscountamount) {
        this.vehiclediscountamount = vehiclediscountamount;
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

    public Double getVehicleprice() {
        return vehicleprice;
    }

    public void setVehicleprice(Double vehicleprice) {
        this.vehicleprice = vehicleprice;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public users getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(users confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Long getPoid() {
        return poid;
    }

    public void setPoid(Long poid) {
        this.poid = poid;
    }

    public Double getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(Double amountpaid) {
        this.amountpaid = amountpaid;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getConfirmedById() {
        return confirmedById;
    }

    public void setConfirmedById(Long confirmedById) {
        this.confirmedById = confirmedById;
    }

    public Long getSuppliernumber() {
        return suppliernumber;
    }

    public void setSuppliernumber(Long suppliernumber) {
        this.suppliernumber = suppliernumber;
    }

    public String getRejectionreason() {
        return rejectionreason;
    }

    public void setRejectionreason(String rejectionreason) {
        this.rejectionreason = rejectionreason;
    }

    public String getSupplierVatnumber() {
        return supplierVatnumber;
    }

    public void setSupplierVatnumber(String supplierVatnumber) {
        this.supplierVatnumber = supplierVatnumber;
    }

    public RoleEnum getRolecratedby() {
        return rolecratedby;
    }

    public void setRolecratedby(RoleEnum rolecratedby) {
        this.rolecratedby = rolecratedby;
    }
}
