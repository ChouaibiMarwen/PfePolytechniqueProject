package com.camelsoft.rayaserver.Request.project;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ProductRequest {
    private Long id ;
    private String name ;
    private Double quantity = 0D;
    private Double unitprice = 0D;
    private Double taxespercentage = 0D;
    private Double discountpercentage = 0D;
    private Double subtotal = 0D;
}
