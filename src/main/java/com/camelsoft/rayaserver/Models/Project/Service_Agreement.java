package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.User.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
public class Service_Agreement implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name ="title")
    private String title;
    @Column(columnDefinition = "TEXT",name = "content")
    private String content;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "supplier_id_serviceagreement")
    private Supplier supplier;
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "timestamp")
    private Date timestamp;

    @Transient
    private Long supplierId;


    public Service_Agreement() {
        this.timestamp = new Date();
    }

    @PostLoad
    private void afterload(){
        if(this.supplier!=null){
            this.supplierId = this.supplier.getId();
        }else{
            this.supplierId = 0L;
        }
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
