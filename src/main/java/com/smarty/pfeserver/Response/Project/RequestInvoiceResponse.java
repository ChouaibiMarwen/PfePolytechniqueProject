package com.smarty.pfeserver.Response.Project;

import com.smarty.pfeserver.Enum.Project.Invoice.InvoiceStatus;
import lombok.Data;

@Data
public class RequestInvoiceResponse {
    private Long id;
    private InvoiceStatus status;
    private Integer invoicenumber;

}
