package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {

}
