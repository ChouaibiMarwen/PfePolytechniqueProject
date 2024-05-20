package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Service_Agreement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAgreementRepository extends JpaRepository<Service_Agreement, Long> {

    Page<Service_Agreement>findByPurchaseorderAndDeletedIsFalse(Pageable page, PurshaseOrder po);
    List<Service_Agreement>findByPurchaseorderAndDeletedIsFalse(PurshaseOrder po);

    Page<Service_Agreement> findByPurchaseorderAndTitleContainingIgnoreCaseAndArchiveIsFalse(Pageable page, PurshaseOrder po, String name);
    List<Service_Agreement> findAllByTitleContainingIgnoreCaseAndDeletedIsFalse(String name);

}
