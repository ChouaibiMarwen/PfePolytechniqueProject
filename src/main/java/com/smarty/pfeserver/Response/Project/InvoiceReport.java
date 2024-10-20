package com.smarty.pfeserver.Response.Project;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceReport {
    private Integer invoicepermonth = 0;
    private Integer refundbymonth = 0;
    private Integer paymentbymonth = 0;
    private Integer purshaseorderrequest = 0;
    private Integer requestdone = 0;
    private Integer requestpending = 0;
    private Integer soldcars = 0;
    private Date date= new Date();
}
