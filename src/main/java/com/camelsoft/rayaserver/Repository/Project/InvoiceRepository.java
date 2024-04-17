package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Page<Invoice> findAllByStatusAndArchiveIsFalse(Pageable page, InvoiceStatus status);
    Page<Invoice> findAllByStatusAndArchiveIsFalseAndRelated(Pageable page, InvoiceStatus status, InvoiceRelated related);
    Page<Invoice> findAllByArchiveIsFalseAndRelated(Pageable page, InvoiceRelated related);
    boolean existsByInvoicenumber(Integer invoicenumber);

}
