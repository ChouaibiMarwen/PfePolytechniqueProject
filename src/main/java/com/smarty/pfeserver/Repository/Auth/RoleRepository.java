package com.smarty.pfeserver.Repository.Auth;

 import com.smarty.pfeserver.Enum.User.RoleEnum;
 import com.smarty.pfeserver.Models.Auth.Role;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleEnum role);
    boolean existsByRole(RoleEnum role);
    List<Role> findByRoleIn(List<RoleEnum> roles);
}
