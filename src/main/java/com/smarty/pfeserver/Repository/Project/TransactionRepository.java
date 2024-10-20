package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Models.Project.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Integer countByStatus(TransactionEnum status);
}
