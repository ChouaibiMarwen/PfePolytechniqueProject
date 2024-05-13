package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.RefundStatus;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "Refund_Invoice")
public class RefundInvoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    private Double amount;
    @Column(name = "status")
    private RefundStatus refundStatus = RefundStatus.INITIATED;
    @Column(columnDefinition = "TEXT",name = "reason")
    private String reason;
    @Column(name = "timestamp")
    private Date timestamp;
    @JsonIgnore
    @Column(name = "archive")
    private Boolean archive = false;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "invoice_id_refund")
    private Invoice invoice;

    public RefundInvoice() {
        this.timestamp = new Date();
    }
}
