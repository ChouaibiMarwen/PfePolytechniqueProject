package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierClassificationRepository extends JpaRepository<SuppliersClassification, Long> {
    Page<SuppliersClassification> findByArchiveIsFalse(Pageable page);
    List<SuppliersClassification> findByArchiveIsFalse();
    List<SuppliersClassification> findByArchiveIsFalseAndNameContainingIgnoreCase(String name);
    Page<SuppliersClassification> findByArchiveIsFalseAndNameContainingIgnoreCase(Pageable page,String name);
    boolean existsByNameIgnoreCase(String name);
}
