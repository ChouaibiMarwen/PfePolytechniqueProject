package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanType;
import com.camelsoft.rayaserver.Enum.Project.Loan.MaritalStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.WorkSector;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import lombok.Data;

import java.util.Date;
@Data

public class LoanRequest {
    private String englishfirstname;
    private String englishlastname;
    private String phonenumber;
    private Double loanamount;
    private String currency;
    private LoanType loantype;
}
