package com.camelsoft.rayaserver.Response.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import lombok.Data;

@Data
public class RequestInvoiceResponse {
    private Long id;
    private InvoiceStatus status;
    private Integer invoicenumber;

}
