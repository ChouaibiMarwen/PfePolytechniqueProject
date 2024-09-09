package com.camelsoft.rayaserver.Request.Tools;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BankInformationRequest {
    private String bank_name ;
    private String accountHolderName ;
    private String  iban ;
    private String acountNumber;


    public BankInformationRequest() {   }




}
