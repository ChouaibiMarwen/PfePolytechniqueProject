package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.DTO.LoanDto;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Repository.Project.LoanRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoanServices {
    @Autowired
    private LoanRepository repository;


    public Loan Save(Loan model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Loan Update(Loan model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Loan FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse FindAllPg(int page, int size) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Loan> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<Loan> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public boolean ExistById(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public boolean ExistByIdAndSupplier(Long id, Supplier supplier) {
        try {
            return this.repository.existsByIdAndSupplier(id, supplier);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public void DeleteById(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllByStateAndDatenNewerThen(int page, int size, LoanStatus status , Date date) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Loan> pckge;
            if (status == null &&  date == null){
               return  FindAllPg(page, size);
            }
            else if(status != null &&  date == null){
                pckge = this.repository.findAllByStatusAndArchiveIsFalse(pg, status);
            }else if (status == null && date != null ) {
                pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
            } else{
              pckge = this.repository.findAllByStatusAndArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg, status, date);
            }

            // Convert Page<Loan> to Page<LoanDto>
            Page<LoanDto> loanDtoPage = pckge.map(loan -> {
                LoanDto dto = new LoanDto();
                dto.mapLoanToDto(loan);
                return dto;
            });

            return new DynamicResponse(loanDtoPage.getContent(), loanDtoPage.getNumber(), loanDtoPage.getTotalElements(), loanDtoPage.getTotalPages());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Integer totalNotArchiveLoans(){
        return this.repository.countAllByArchiveIsFalse();
    }

    public Double totalLoansAmounts(){
        Double sum = 0.0;
        List<Loan> list = this.repository.findByArchiveIsFalse();
        for (Loan loan : list) {
            sum += loan.getLoanamount();
        }
        return sum;

    }



    public DynamicResponse FindAllByStateAndSupplier(int page, int size, LoanStatus status, Supplier supplier) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Loan> pckge = this.repository.findAllByStatusAndSupplierAndArchiveIsFalse(pg, status, supplier);
           // List<Loan> list = this.repository.findAllByStatusAndSupplierAndArchiveIsFalse(status, supplier);
            // Convert Page<Loan> to Page<LoanDto>
           /* Page<LoanDto> loanDtoPage = pckge.map(loan -> {
                LoanDto dto = new LoanDto();
                dto.mapLoanToDto(loan);
                return dto;
            });*/

            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllBySupplier(int page, int size, Supplier supplier) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Loan> pckge = this.repository.findAllBySupplierAndArchiveIsFalse(pg, supplier);
            // Convert Page<Loan> to Page<LoanDto>
            Page<LoanDto> loanDtoPage = pckge.map(loan -> {
                LoanDto dto = new LoanDto();
                dto.mapLoanToDto(loan);
                return dto;
            });
            return new DynamicResponse(loanDtoPage.getContent(), loanDtoPage.getNumber(), loanDtoPage.getTotalElements(), loanDtoPage.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public long countByStatus(LoanStatus status) {
        return this.repository.countByStatus(status);
    }
    public Integer countLoneDoneBySupplier(Supplier supplier) {
        return this.repository.countAllByStatusAndSupplierAndArchiveIsFalse(LoanStatus.DONE, supplier);
    }

    public Integer countLoneInProgressBySupplier(Supplier supplier) {
        List<LoanStatus> statuses = Arrays.asList(LoanStatus.WAITING, LoanStatus.IN_PROGRESS);
        return this.repository.countAllBySupplierAndArchiveIsFalseAndStatusIn(supplier, statuses);
    }


    public Double sumLoanAmountForDoneLoans() {
        Double sum = this.repository.sumLoanAmountByStatus(LoanStatus.DONE);
        return sum != null ? sum : 0.0;
    }


    public Double sumLoanAmountByStatus(LoanStatus status) {
        Double sum = this.repository.sumLoanAmountByStatus(status);
        return sum != null ? sum : 0.0;

    }

}
