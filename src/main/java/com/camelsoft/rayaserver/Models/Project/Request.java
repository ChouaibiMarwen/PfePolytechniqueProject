package com.camelsoft.rayaserver.Models.Project;


import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "type")
    private String type ;
    @Column(name = "status")
    private InvoiceStatus status = InvoiceStatus.UNPAID;
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestCorrespondence> corssspondences = new ArrayList<>();

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




}
