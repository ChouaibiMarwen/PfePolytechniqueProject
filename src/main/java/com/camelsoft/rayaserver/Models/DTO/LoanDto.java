package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanType;
import com.camelsoft.rayaserver.Models.Project.Loan;
import lombok.Data;

import java.util.Date;

@Data
public class LoanDto {
    private Long id;
    private String englishfirstname;
    private String englishlastname;
    private String phonenumber;
    private Double loanamount;
    private String currency;
    private LoanType loantype;
    private Date timestamp;
    private Long idcreatorid;
    private String creatorname;
    private LoanStatus status;


    public static LoanDto mapLoanToDto(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setEnglishfirstname(loan.getEnglishfirstname()) ;
        dto.setEnglishlastname(loan.getEnglishlastname()) ;
        dto.setPhonenumber(loan.getPhonenumber()) ;
        dto.setLoanamount(loan.getLoanamount()) ;
        dto.setCurrency(loan.getCurrency()) ;
        dto.setLoantype(loan.getLoantype()) ;
        dto.setTimestamp(loan.getTimestamp()) ;
        dto.setStatus(loan.getStatus()) ;
        dto.setId(loan.getId()) ;
        if(loan.getSupplier() != null){
            dto.creatorname = loan.getSupplier().getName();
            dto.idcreatorid = loan.getSupplier().getId();
        }

        return dto;
    }
}
