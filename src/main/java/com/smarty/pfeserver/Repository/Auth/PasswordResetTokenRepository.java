package com.smarty.pfeserver.Repository.Auth;


import com.smarty.pfeserver.Models.Auth.PasswordResetToken;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByUser(users user);
    List<PasswordResetToken> findAllByUser(users user);
    PasswordResetToken findByToken(String token);
}
