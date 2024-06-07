package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Data
@Entity
public class PurshaseOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "order_date")
    private Date orderDate;
    @Getter
    @Column(name = "order_status")
    private PurshaseOrderStatus status = PurshaseOrderStatus.PENDING;
    @Column(name = "supplier_id")
    private Long supplierId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicles_id")
    private Vehicles vehicles;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id_purchaseorder", nullable = false)
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private users customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby_id")
    private users createdby;
    @Column(name = "vehicule_id")
    private Long vehicleId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "discount_amount")
    private Double discountamount = 0D;
    @Column(name = "request_delivered_date")
    private Date requestDeliveryDate;
    @Column(name = "street")
    private String streetAddress;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "postal_code")
    private String codePostal;
    @Column(name = "contry")
    private String country;
    @Column(columnDefinition = "TEXT", name = "description")
    private String description;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "attachments")
    private Set<MediaModel> attachments = new HashSet<>();
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @JsonIgnore
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Transient
    private Integer poCountBySupplier = 0;
    @Transient
    private Long invoiceid;
    @Transient
    private Boolean haveinvoice = false;
    @OneToOne(mappedBy = "purshaseorder")
    @JsonIgnore
    private Invoice invoice;

    @Column(name = "timestamp")
    private Date timestamp;

    public PurshaseOrder() {
        this.timestamp = new Date();
    }


    @PostLoad
    private void afterload() {
        if (this.supplier != null) {
            if (this.supplier.getPurchaseOrders() != null) {
                this.poCountBySupplier = this.supplier.getPurchaseOrders().size();

            } else {
                this.poCountBySupplier = 0;
            }
        }
        if (invoice != null) {
            this.invoiceid = invoice.getId();
            this.haveinvoice = true;
        } else {
            this.haveinvoice = false;
        }
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
