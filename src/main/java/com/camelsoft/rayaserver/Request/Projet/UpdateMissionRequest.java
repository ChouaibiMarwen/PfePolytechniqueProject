package com.camelsoft.rayaserver.Request.Projet;

import com.camelsoft.rayaserver.Enum.Project.MissionStatusEnum;

import java.util.Date;

public class UpdateMissionRequest {
    private Date startdate;
    private String title;
    private String address;
    private Date enddate;
    private Long idTeamLead;
    private Double locationLatitude;
    private Double locationLongitude;
    private String locationname;
    private Double budget;
    private MissionStatusEnum status;

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getIdTeamLead() {
        return idTeamLead;
    }

    public void setIdTeamLead(Long idTeamLead) {
        this.idTeamLead = idTeamLead;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public MissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MissionStatusEnum status) {
        this.status = status;
    }
}
