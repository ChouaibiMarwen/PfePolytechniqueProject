package com.camelsoft.rayaserver.Repository.User;

import com.camelsoft.rayaserver.Models.Project.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {
}
