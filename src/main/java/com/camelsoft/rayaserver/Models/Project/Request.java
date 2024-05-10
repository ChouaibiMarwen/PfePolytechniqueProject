package com.camelsoft.rayaserver.Models.Project;


import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Response.Project.RequestInvoiceResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "type")
    private String type ;
    @Column(name = "status")
    private RequestState status = RequestState.NONE;
    @JsonIgnore
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RequestCorrespondence> corssspondences = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id_creatorrequest", nullable = false)
    private users creatorrequest;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "request_invoices",
            joinColumns =
            @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "invoice_id", referencedColumnName = "id"))
    private Set<Invoice> invoices = new HashSet<>();
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "timestamp")
    private Date timestamp;



    public Request() {
        this.timestamp = new Date();
    }





    public Request(String type, RequestState status, users creatorrequest, Set<Invoice> invoices) {
        this.type = type;
        this.status = status;
        this.creatorrequest = creatorrequest;
        this.invoices = invoices;
        this.timestamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RequestState getStatus() {
        return status;
    }

    public void setStatus(RequestState status) {
        this.status = status;
    }

    public Set<RequestCorrespondence> getCorssspondences() {
        return corssspondences;
    }

    public void setCorssspondences(Set<RequestCorrespondence> corssspondences) {
        this.corssspondences = corssspondences;
    }

    public users getCreatorrequest() {
        return creatorrequest;
    }

    public void setCreatorrequest(users creatorrequest) {
        this.creatorrequest = creatorrequest;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
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
