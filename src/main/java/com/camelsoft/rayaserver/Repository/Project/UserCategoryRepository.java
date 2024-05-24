package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCategoryRepository extends JpaRepository<UsersCategory, Long> {
    Page<UsersCategory> findByArchiveIsFalse(Pageable page);
    List<UsersCategory> findByArchiveIsFalse();
    List<UsersCategory> findByArchiveIsFalseAndNameContainingIgnoreCase(String name);
    List<UsersCategory> findByArchiveIsFalseAndCategoryrole(RoleEnum role);
    @Query("SELECT uc FROM UsersCategory uc "
            + "JOIN uc.users u "
            + "WHERE u.id = :userId")
    List<UsersCategory> findByUserId( Long userId);

    @Query("SELECT uc FROM UsersCategory uc "
            + "JOIN uc.users u "
            + "WHERE u.id = :userId")
    Page<UsersCategory> findByUserIdPg(Pageable page, Long userId);
}
