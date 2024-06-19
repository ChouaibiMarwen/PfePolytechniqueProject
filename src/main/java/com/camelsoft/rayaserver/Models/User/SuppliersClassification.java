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
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "archive")
    private Boolean archive = false;
    @JsonIgnore
    @OneToMany(mappedBy = "supplierclassification" ,fetch = FetchType.EAGER)
    private Set<users> suppliers = new HashSet<>();

}
