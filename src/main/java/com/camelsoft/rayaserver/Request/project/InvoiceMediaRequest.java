package com.camelsoft.rayaserver.Request.project;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InvoiceMediaRequest {
    private MultipartFile estimarafile;
    private MultipartFile deliverynotedocument;
    private MultipartFile supplierInvoice;
}
