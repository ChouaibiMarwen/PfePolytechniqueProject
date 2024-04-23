package com.camelsoft.rayaserver.Models.User;


import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "supplier_number")
    private Long suppliernumber;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Loan> loans = new HashSet<>();


    @Transient
    private String name;

    @Transient
    private Long userId;

    @Transient
    private Double ratingAverage;

    @Transient
    private Integer ratingCount;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "supplier_files_id")
    private Set<File_model> images = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToOne(mappedBy = "supplier")
    @JsonIgnore
    private users user;

    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY ,orphanRemoval = true)
    private Set<Vehicles> vehicles = new HashSet<>();


}
