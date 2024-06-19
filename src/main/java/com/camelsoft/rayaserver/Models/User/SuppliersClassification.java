package com.camelsoft.rayaserver.Models.User;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "UserCategory")
public class SuppliersClassification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(columnDefinition = "TEXT",name = "description")
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "supplierclassification" ,fetch = FetchType.EAGER)
    private Set<users> suppliers = new HashSet<>();
    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @Column(name = "archive")
    private Boolean archive = false;

    public SuppliersClassification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<users> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<users> suppliers) {
        this.suppliers = suppliers;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }
}
