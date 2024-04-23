package com.camelsoft.rayaserver.Repository.User;


import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<users, Long> {




    users findByEmail(String email);

    users findByPhonenumber(String phonenumber);

    boolean existsByEmail(String email);

    boolean existsByPhonenumber(String phonenumber);

    boolean existsById(Long id);

    users findByUsername(String username);




    users findByRole(Role role);

    Page<users> findAllByRoleAndActiveAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, Boolean deleted);

    Page<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(Pageable page, Role role, String name, Boolean delete, String userNaMe);



    Page<users> findByRoleAndActiveAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, Boolean deleted);

    Page<users> findByRoleAndActiveOrderBySupplier_RatingsDesc(Pageable page, Role role, Boolean active);

    Page<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, String name, Boolean deleted);

    Page<users> findAllByRoleAndNameContainingIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(Pageable page, Role role, String name, Boolean delete, String userName);

    Page<users> findByRoleOrderByTimestmpDesc(Pageable page, Role role);

    Page<users> findByRoleAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(Pageable page, Role role, Boolean aBoolean, String userName);

    Page<users> findByRoleOrderBySupplier_RatingsDesc(Pageable page, Role role);

    List<users> findAllByRole(Role role);

    users findTopByRole(Role role);

    Long countAllByRole(Role role);

    Long countAllByRoleAndVerified(Role role, Boolean verified);

    Long countAllByRoleAndActive(Role role, Boolean active);

    List<users> findAllByRoleAndActive(Role role, Boolean active);

    Page<users> findAllByNameContains(Pageable page, String name);
}
