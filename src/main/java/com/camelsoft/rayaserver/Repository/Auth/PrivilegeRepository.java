package com.camelsoft.rayaserver.Repository.Auth;


import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
    Boolean existsByName(String name);
    Boolean existsByIdAndUser(Long id, users user);
}
