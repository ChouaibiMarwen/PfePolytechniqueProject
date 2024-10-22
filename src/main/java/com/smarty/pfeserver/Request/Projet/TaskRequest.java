package com.smarty.pfeserver.Request.Projet;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class TaskRequest {
    private Date startdate;
    private String title;
    private String description;
    private Date enddate;
    private List<Long> idParticipants;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public List<Long> getIdParticipants() {
        return idParticipants;
    }

    public void setIdParticipants(List<Long> idParticipants) {
        this.idParticipants = idParticipants;
    }
}
