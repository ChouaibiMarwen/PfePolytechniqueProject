package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurshaseOrderRepository extends JpaRepository<PurshaseOrder, Long> {
    Page<PurshaseOrder> findAllByArchiveIsFalse(Pageable page);

    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatus(Pageable page, PurshaseOrderStatus status);


}
