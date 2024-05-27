package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
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
@Table(name = "Payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "amount")
    private Double amount = 0.0;
    @Column(columnDefinition = "TEXT", name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_payment")
    private Invoice invoice;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "payment-proof_id")
    private Set<File_model> attachments = new HashSet<>();
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Set<File_model> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<File_model> attachments) {
        this.attachments = attachments;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
