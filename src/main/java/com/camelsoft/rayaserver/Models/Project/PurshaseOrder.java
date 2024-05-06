package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Data
@Entity
public class PurshaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name ="order_date")
    private Date orderDate;
    @Getter
    @Column(name ="order_status")
    private PurshaseOrderStatus status = PurshaseOrderStatus.PENDING;
    @Column(name ="supplier_id")
    private Long supplierId;
    @Column(name ="vehicule_id")
    private Long vehicleId;
    @Column(name ="quantity")
    private Integer quantity;
    @Column(name ="discount_amount")
    private Double discountamount = 0D;
    @Column(name ="request_delivered_date")
    private Date requestDeliveryDate;
    @Column(name ="street")
    private String streetAddress;
    @Column(name ="city")
    private String city;
    @Column(name ="state")
    private String state;
    @Column(name ="postal_code")
    private String codePostal;
    @Column(name ="contry")
    private String country;
    @Column(columnDefinition = "TEXT",name = "description")
    private String description;
    @OneToMany(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "attachments")
    private Set<File_model> attachments = new HashSet<>();
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;

    @Column(name = "timestamp")
    private Date timestamp;

    public PurshaseOrder()  {
        this.timestamp=new Date();
    }


    public PurshaseOrder(Date orderDate, PurshaseOrderStatus status, Long supplierId, Integer quantity, Long vehicleId, Double discountamount, Date requestDeliveryDate, String streetAddress, String city, String codePostal, String state, String country, String description) {
        this.orderDate = orderDate;
        this.status = status;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.vehicleId = vehicleId;
        this.discountamount = discountamount;
        this.requestDeliveryDate = requestDeliveryDate;
        this.streetAddress = streetAddress;
        this.city = city;
        this.codePostal = codePostal;
        this.state = state;
        this.country = country;
        this.description = description;
        this.timestamp = new Date();
    }
}
