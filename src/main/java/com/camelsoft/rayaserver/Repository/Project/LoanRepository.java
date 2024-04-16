package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.LoanStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Page<Loan> findAllByStatus(Pageable page, LoanStatus status);
    boolean existsByIdAndSupplier(Long id , Supplier supplier);
    Page<Loan> findAllByStatusAndSupplier(Pageable page, LoanStatus status,Supplier supplier);
    Page<Loan> findAllBySupplier(Pageable page, Supplier supplier);

}
