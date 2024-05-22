package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.Service_Agreement;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAgreementRepository extends JpaRepository<Service_Agreement, Long> {

    Page<Service_Agreement>findBySupplierAndDeletedIsFalse(Pageable page, Supplier supplier);
    List<Service_Agreement>findBySupplierAndDeletedIsFalse(Supplier supplier);

    List<Service_Agreement> findAllByTitleContainingIgnoreCaseAndDeletedIsFalse(String name);

}
