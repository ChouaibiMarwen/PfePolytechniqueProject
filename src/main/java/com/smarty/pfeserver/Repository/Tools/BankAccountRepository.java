package com.smarty.pfeserver.Repository.Tools;

import com.smarty.pfeserver.Models.Tools.BankInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository  extends JpaRepository<BankInformation, Long> {
}
