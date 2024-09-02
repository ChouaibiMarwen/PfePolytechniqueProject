package com.camelsoft.rayaserver.Request.Tools;

import org.springframework.web.multipart.MultipartFile;

public class BankInformationRequest {
    private String bank_name ;
    private String accountHolderName ;
    private String  IBAN ;
    private String acountNumber;


    public BankInformationRequest() {   }


    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getAcountNumber() {
        return acountNumber;
    }

    public void setAcountNumber(String acountNumber) {
        this.acountNumber = acountNumber;
    }

}
