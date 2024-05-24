package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Page<Invoice> findAllByStatusAndArchiveIsFalse(Pageable page, InvoiceStatus status);
    Page<Invoice> findAllByStatusAndArchiveIsFalseAndRelated(Pageable page, InvoiceStatus status, InvoiceRelated related);
    Page<Invoice> findAllByStatusAndArchiveIsFalseAndRelatedAndCreatedby(Pageable page, InvoiceStatus status, InvoiceRelated related,users user);
    Page<Invoice> findAllByArchiveIsFalseAndRelated(Pageable page, InvoiceRelated related);
    boolean existsByInvoicenumber(Integer invoicenumber);
    Integer countByTimestampBetweenAndRelated(Date startDate, Date endDate,InvoiceRelated related);
    Integer countByTimestampBetweenAndStatusAndRelated(Date startDate, Date endDate,InvoiceStatus status,InvoiceRelated related);
    List<Invoice> findByStatus(InvoiceStatus status);

    @Query("SELECT SUM(p.subtotal) FROM Invoice i JOIN i.products p WHERE i.invoicedate BETWEEN :startDate AND :endDate AND i.status = 'PAID'")
    Double sumSubtotalOfProductsByInvoiceDateBetween(Date startDate, Date endDate);

    List<Invoice> findByStatusAndArchiveIsFalseAndTimestampBetween(InvoiceStatus status, Date startDate, Date endDate);
    List<Invoice> findByStatusAndArchiveIsFalseAndTimestampBetweenAndRelatedto(InvoiceStatus status, Date startDate, Date endDate, users user);
    List<Invoice> findByStatusAndArchiveIsFalseAndTimestampBetweenAndCreatedby(InvoiceStatus status, Date startDate, Date endDate, users user);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status AND i.createdby.role.role = :role")
    long countByStatusAndCreatedByRole(InvoiceStatus status, RoleEnum role);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.createdby.role.role = :role")
    long countByCreatedByRole(RoleEnum role);

    @Query("SELECT COUNT(i) FROM Invoice i " +
            "WHERE i.status IN (:statuses) " +
            "AND i.createdby.role.role = :role")
    long countUnpaidOrPartiallyPaidInvoicesByCreatedByRole (Set<InvoiceStatus> statuses, RoleEnum role);


    @Query("SELECT i FROM Invoice i " +
            "WHERE i.status = :status " +
            "AND i.archive = false " +
            "AND i.timestamp BETWEEN :startDate AND :endDate " +
            "AND i.createdby.role.role = :role")
    List<Invoice> findByStatusAndArchiveIsFalseAndTimestampBetweenAndCreatedByRole(InvoiceStatus status, Date startDate, Date endDate, RoleEnum role);

    Page<Invoice> findByCreatedbyOrRelatedto(users user,users userRelated ,Pageable pageable);
}
