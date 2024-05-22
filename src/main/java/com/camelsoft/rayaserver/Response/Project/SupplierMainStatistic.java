package com.camelsoft.rayaserver.Response.Project;

import lombok.Data;

@Data
public class SupplierMainStatistic {
    private Integer totalcars = 0;
    private Double pendingpayment = 0D;
    private Double salesrevenue = 0D;
    private Integer soldcars = 0;
    private Integer loaninprogress = 0;
    private Integer loandone = 0;

    public SupplierMainStatistic() {
    }
}
