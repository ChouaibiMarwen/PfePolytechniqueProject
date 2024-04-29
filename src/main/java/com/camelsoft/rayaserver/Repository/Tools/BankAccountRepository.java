package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository  extends JpaRepository<BankInformation, Long> {
}
