package com.smarty.pfeserver.Request.Projet;

public class BudgetRequest {
    private Double amount;
    private Long idMission;

    public Long getIdMission() {
        return idMission;
    }

    public void setIdMission(Long idMission) {
        this.idMission = idMission;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
