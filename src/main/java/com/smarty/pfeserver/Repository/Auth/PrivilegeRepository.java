package com.smarty.pfeserver.Repository.Auth;


import com.smarty.pfeserver.Models.Auth.Privilege;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
    Boolean existsByName(String name);
    Boolean existsByIdAndUser(Long id, users user);
    Privilege findByName(String name);
}
