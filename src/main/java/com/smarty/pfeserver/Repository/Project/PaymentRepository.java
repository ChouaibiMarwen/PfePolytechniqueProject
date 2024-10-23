package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}