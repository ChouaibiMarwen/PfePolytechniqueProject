package com.camelsoft.rayaserver.Models.Project;


import com.camelsoft.rayaserver.Enum.Project.MissionStatusEnum;
import com.camelsoft.rayaserver.Enum.TransactionEnum;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "createdby")
    private users createdby;
    @Column(name = "status")
    private TransactionEnum status = TransactionEnum.PENDING;
    @Column(name = "name")
    private String name ;
    @Column(name = "description")
    private String description ;
    @Column(name = "rejectionreason" , columnDefinition = "TEXT")
    private String rejectionreason ;
    @Column(name = "amount")
    private Double amount;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "itansactions_mission")
    private Mission mission;
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "timestmp")
    private Date timestamp = new Date();
    @Transient
    private String missiontitle;
    @Transient
    private Long missionid;

    @Transient
    private Long createdbyid;

    @PostLoad
    public void postLoad() {
        if(this.mission != null){
            this.missiontitle = this.mission.getTitle();
            this.missionid = this.mission.getId();
        }
        if(this.createdby != null){
            this.createdbyid = this.createdby.getId();
        }
    }

    public Transaction() {
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

    public TransactionEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionEnum status) {
        this.status = status;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getMissiontitle() {
        return missiontitle;
    }

    public void setMissiontitle(String missiontitle) {
        this.missiontitle = missiontitle;
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }

    public Long getCreatedbyid() {
        return createdbyid;
    }

    public void setCreatedbyid(Long createdbyid) {
        this.createdbyid = createdbyid;
    }

    public String getRejectionreason() {
        return rejectionreason;
    }

    public void setRejectionreason(String rejectionreason) {
        this.rejectionreason = rejectionreason;
    }
}
