package com.camelsoft.rayaserver.Repository.User;


import com.camelsoft.rayaserver.Models.User.UserSession;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long> {

    Page<UserSession> findAllByUser(Pageable pageable, users user);

}
