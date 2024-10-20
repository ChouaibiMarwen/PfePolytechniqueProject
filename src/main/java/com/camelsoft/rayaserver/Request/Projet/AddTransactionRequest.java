package com.camelsoft.rayaserver.Request.Projet;

import com.camelsoft.rayaserver.Enum.TransactionEnum;
import com.camelsoft.rayaserver.Models.Project.Mission;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

public class AddTransactionRequest {
    private String name ;
    private String description ;
    private Double amount;
   private Long missionId ;

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

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }
}
