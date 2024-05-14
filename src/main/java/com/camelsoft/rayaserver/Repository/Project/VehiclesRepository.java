package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles,Long> {
    Page<Vehicles> findAllByArchiveIsFalse(Pageable page);
    Page<Vehicles> findAllByArchiveIsFalseAndSupplier(Pageable page, Supplier supplier);
    Page<Vehicles> findAllByArchiveIsFalseAndCarvinContainingIgnoreCaseAndSupplier(Pageable page, String carvin,Supplier supplier);

}
