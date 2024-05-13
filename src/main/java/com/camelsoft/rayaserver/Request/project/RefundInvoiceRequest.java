package com.camelsoft.rayaserver.Request.project;

import lombok.Data;

@Data
public class RefundInvoiceRequest {

    private String reason;
    private Double amount;


}
