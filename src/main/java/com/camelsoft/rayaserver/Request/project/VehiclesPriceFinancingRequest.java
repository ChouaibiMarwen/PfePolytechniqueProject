package com.camelsoft.rayaserver.Request.project;

import lombok.Data;



@Data
public class VehiclesPriceFinancingRequest {
    private Double price = 0D;
    private String currency = "SAR";
    private Boolean discount =false;
    private Double discountpercentage = 0D;
    private Double discountamount = 0D;
    private Double vatpercentage = 0D;
    private Double vatamount = 0D;
    private Double totalamount = 0D;
}
