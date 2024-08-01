package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanType;
import com.camelsoft.rayaserver.Models.File.MediaModel;
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
    private MediaModel attachment;
    private String rejectraison;
    private Long idprocessedby;
    private String nameprocessedby;
    private Long vehicleid;


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
        dto.setAttachment(loan.getAttachment()) ;
        dto.setRejectraison(loan.getRejectraison());
        if(loan.getSupplier() != null){
            dto.creatorname = loan.getSupplier().getName();
            dto.idcreatorid = loan.getSupplier().getId();
        }
        if(loan.getProcessedby() != null){
            dto.setIdprocessedby(loan.getProcessedby().getId());
            dto.setNameprocessedby(loan.getProcessedby().getName());
        }
        if(loan.getVehicle() != null)
            dto.setVehicleid(loan.getVehicle().getId());

        return dto;
    }
}
