package com.smarty.pfeserver.Repository.Tools;

import com.smarty.pfeserver.Models.Tools.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
