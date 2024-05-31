package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Models.Project.Product;
import lombok.Data;

import java.util.*;

@Data
public class InvoiceRequest {
    private Integer invoicenumber = 0;
    private InvoiceStatus status = InvoiceStatus.UNPAID;
    private Date invoicedate = new Date();
    private Date duedate = new Date();
    private String currency="SAR";
    private String suppliername;
    private String supplierzipcode;
    private String supplierstreetadress;
    private String supplierphonenumber;
    private String bankname;
    private String bankzipcode;
    private String bankstreetadress;
    private String bankphonenumber;
    private String vehicleregistration;
    private String vehiclecolor;
    private String vehiclevin;
    private String vehiclemodel;
    private String vehiclemark;
    private String vehiclemileage;
    private String vehiclemotexpiry;
    private String vehicleenginesize;
    private String bankiban;
    private String thirdpartypoid;
    private String bankrip;
    private InvoiceRelated related=InvoiceRelated.NONE;
    private Long relatedtouserid;
    private List<Long> products = new ArrayList<>();
}
