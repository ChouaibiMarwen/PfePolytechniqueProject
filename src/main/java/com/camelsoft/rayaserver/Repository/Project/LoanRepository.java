package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Page<Loan> findAllByStatusAndArchiveIsFalse(Pageable page, LoanStatus status);
    Page<Loan> findAllByStatusAndArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable pageable, LoanStatus status, Date timestamp);
    Page<Loan> findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable pageable,Date timestamp);
    boolean existsByIdAndSupplier(Long id , Supplier supplier);
    Page<Loan> findAllByStatusAndSupplierAndArchiveIsFalse(Pageable page, LoanStatus status,Supplier supplier);
    List<Loan> findAllByStatusAndSupplierAndArchiveIsFalse(LoanStatus status,Supplier supplier);
    Page<Loan> findAllBySupplierAndArchiveIsFalse(Pageable page, Supplier supplier);
    List<Loan> findAllBySupplierAndArchiveIsFalse(Supplier supplier);
    long countByStatus(LoanStatus status);

    @Query("SELECT SUM(l.loanamount) FROM Loan l WHERE l.status = :status")
    Double sumLoanAmountByStatus(@Param("status") LoanStatus status);

    Integer countAllByStatusAndSupplierAndArchiveIsFalse(LoanStatus status, Supplier supplier);

    Integer countAllBySupplierAndArchiveIsFalseAndStatusIn(Supplier supplier, List<LoanStatus> statuses);
    Integer countAllByArchiveIsFalse();
    List<Loan> findByArchiveIsFalse();





}