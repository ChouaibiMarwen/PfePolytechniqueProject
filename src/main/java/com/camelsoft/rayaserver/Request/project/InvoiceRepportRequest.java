package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import lombok.Data;

import java.sql.Date;
import java.util.*;

@Data
public class InvoiceRepportRequest {
    private Date date;
    private Date enddate;
    private Long supplierid;
}
