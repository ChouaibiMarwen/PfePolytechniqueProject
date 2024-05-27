package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
