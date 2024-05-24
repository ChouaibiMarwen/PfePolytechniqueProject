package com.camelsoft.rayaserver.Repository.User;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    Page<UserAction> findByUserRoleRole(Pageable page, RoleEnum roleEnum);

    @Query("SELECT ua FROM UserAction ua WHERE ua.user.role.role = :roleEnum " +
            "AND ua.timestamp = (SELECT MAX(ua2.timestamp) FROM UserAction ua2 WHERE ua2.user = ua.user)")
    Page<UserAction> findLatestUserActionsByRole(Pageable page, RoleEnum roleEnum);

    Page<UserAction> findByUserOrderByTimestampDesc(Pageable pageable, users user);

    @Query("SELECT ua " +
            "FROM UserAction ua " +
            "WHERE ua.user.role.role = :roleEnum " +
            "AND (LOWER(ua.user.personalinformation.firstnameen) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "     OR LOWER(ua.user.personalinformation.lastnameen) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND ua.timestamp = (SELECT MAX(ua2.timestamp) FROM UserAction ua2 WHERE ua2.user = ua.user)")
    Page<UserAction> findLatestUserActionsByRoleAndName(Pageable page, RoleEnum roleEnum, String name);

    @Query("SELECT ua " +
            "FROM UserAction ua " +
            "WHERE ua.user.role.role = :roleEnum " +
            "AND ua.user.id = :userId " +
            "AND ua.timestamp = (SELECT MAX(ua2.timestamp) FROM UserAction ua2 WHERE ua2.user = ua.user)")
    Page<UserAction> findLatestUserActionsByRoleAndUserId(Pageable page, RoleEnum roleEnum, Long userId);

}