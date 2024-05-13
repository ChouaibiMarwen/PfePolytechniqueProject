package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PurshaseOrderRepository extends JpaRepository<PurshaseOrder, Long> {
    Page<PurshaseOrder> findAllByArchiveIsFalse(Pageable page);

    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatus(Pageable page, PurshaseOrderStatus status);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,Date date);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status, Date date);


}
