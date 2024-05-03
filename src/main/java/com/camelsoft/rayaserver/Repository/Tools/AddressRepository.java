package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Tools.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
