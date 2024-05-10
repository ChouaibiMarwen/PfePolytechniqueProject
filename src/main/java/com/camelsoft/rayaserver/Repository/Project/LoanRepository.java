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

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Page<Loan> findAllByStatusAndArchiveIsFalse(Pageable page, LoanStatus status);
    boolean existsByIdAndSupplier(Long id , Supplier supplier);
    Page<Loan> findAllByStatusAndSupplierAndArchiveIsFalse(Pageable page, LoanStatus status,Supplier supplier);
    Page<Loan> findAllBySupplierAndArchiveIsFalse(Pageable page, Supplier supplier);
    long countByStatus(LoanStatus status);

    @Query("SELECT SUM(l.loanamount) FROM Loan l WHERE l.status = :status")
    Double sumLoanAmountByStatus(@Param("status") LoanStatus status);

}
