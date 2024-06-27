package com.camelsoft.rayaserver.Repository.User;


import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<users, Long> {




    users findByEmail(String email);

    users findByPhonenumber(String phonenumber);

    boolean existsByEmail(String email);

    boolean existsByPhonenumber(String phonenumber);

    boolean existsById(Long id);

    users findByUsername(String username);
    users findTopByOrderByIdDesc();

    List<users> findByRoleAndSubadminClassificationAndActiveIsTrueAndDeletedIsFalse(Role role, SuppliersClassification classification);

    @Query("SELECT u FROM users u WHERE u.role = :role AND (u.subadminClassification = :classification OR u.subadminClassification IS NULL) AND u.active = true AND u.deleted = false")
    List<users> findbyroleandclassififcztiornullclassification(Role role,SuppliersClassification classification
    );
    Integer countByRoleAndSubadminClassificationAndActiveIsTrue(Role role, SuppliersClassification classification);


    users findByRole(Role role);

    /*Page<users> findAllByRoleAndActiveAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, Boolean deleted);*/

    Page<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(Pageable page, Role role, String name, Boolean delete, String userNaMe);
    List<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(Role role, String name, Boolean delete, String userNaMe);
    List<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndManagerOrderByTimestmpDesc(Role role, String name, Boolean delete, String userNaMe,users user);

    Page<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndVerifiedOrderByTimestmpDesc(Pageable page, Role role, String name, Boolean delete, String userNaMe, Boolean verified);
    List<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndVerifiedOrderByTimestmpDesc(Role role, String name, Boolean delete, String userNaMe, Boolean verified);
    List<users> findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndVerifiedAndManagerOrderByTimestmpDesc(Role role, String name, Boolean delete, String userNaMe, Boolean verified,users user);



    Page<users> findByRoleAndActiveAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, Boolean deleted);
    List<users> findByRoleAndActiveAndDeletedOrderByTimestmpDesc(Role role, Boolean active, Boolean deleted);
    List<users> findByRoleAndActiveAndDeletedAndManagerOrderByTimestmpDesc(Role role, Boolean active, Boolean deleted,users manager);


    Page<users> findByRoleAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean deleted);

    Page<users> findByRoleAndDeletedAndVerifiedOrderByTimestmpDesc(Pageable page, Role role, Boolean deleted, Boolean verified);

    Page<users> findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, Boolean deleted, Boolean verified);
    List<users> findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(Role role, Boolean active, Boolean deleted, Boolean verified);
     List<users> findByRoleAndActiveAndDeletedAndVerifiedAndManagerOrderByTimestmpDesc(Role role, Boolean active, Boolean deleted, Boolean verified,users manger);

    Page<users> findByRoleAndActiveOrderBySupplier_RatingsDesc(Pageable page, Role role, Boolean active);

    Page<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, String name, Boolean deleted);
    List<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(Role role, Boolean active, String name, Boolean deleted);
    List<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndManagerOrderByTimestmpDesc(Role role, Boolean active, String name, Boolean deleted,users user);

    Page<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(Pageable page, Role role, Boolean active, String name, Boolean deleted, Boolean verified);
    List<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(Role role, Boolean active, String name, Boolean deleted, Boolean verified);
    List<users> findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndVerifiedAndManagerOrderByTimestmpDesc (Role role, Boolean active, String name, Boolean deleted, Boolean verified,users manager);

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
    List<users> findByRoleAndDeletedIsFalse(Role role);
    List<users> findByDeletedIsFalseAndActiveIsTrue();
    List<users> findByRoleInAndDeletedIsFalse(List<Role> roles);
    List<users> findByRoleInAndSupplierclassificationIsNullAndDeletedIsFalse(List<Role> roles);


    Page<users> findAllByNameContains(Pageable page, String name);
}
