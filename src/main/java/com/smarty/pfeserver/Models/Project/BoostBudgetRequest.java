package com.smarty.pfeserver.Models.Project;


import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "BoostBudget")
public class BoostBudgetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "createdby")
    private users createdby;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_mission")
    private Mission mission;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "rejectionreason", columnDefinition = "TEXT")
    private String rejectionreason;
    @Column(name = "status")
    private TransactionEnum status = TransactionEnum.PENDING;
    @Column(name = "timestmp")
    private Date timestamp = new Date();

    public BoostBudgetRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public users getCreatedby() {
        return createdby;
    }

    public void setCreatedby(users createdby) {
        this.createdby = createdby;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionEnum status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestmp) {
        this.timestamp = timestmp;
    }

    public String getRejectionreason() {
        return rejectionreason;
    }

    public void setRejectionreason(String rejectionreaso) {
        this.rejectionreason = rejectionreaso;
    }
}
