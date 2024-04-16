package com.camelsoft.rayaserver.Repository.Auth;

 import com.camelsoft.rayaserver.Models.Auth.Role;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
    boolean existsByRole(String role);
}
