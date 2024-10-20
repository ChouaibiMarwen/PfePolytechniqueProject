package com.smarty.pfeserver.Response.Project;

import com.smarty.pfeserver.Enum.TransactionEnum;

public class TransactionByStatus {
    private TransactionEnum status;
    private Integer amount;

    public TransactionByStatus(TransactionEnum status, Integer amount) {
        this.status = status;
        this.amount = amount;
    }

    public TransactionEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionEnum status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
