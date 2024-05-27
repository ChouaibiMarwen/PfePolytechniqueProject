package com.camelsoft.rayaserver.Request.project;

import javax.persistence.Column;

public class PaymentRequest {

    private Double amount = 0.0;

    private String notes;

    public PaymentRequest() {
    }

    public PaymentRequest(Double amount, String notes) {
        this.amount = amount;
        this.notes = notes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
