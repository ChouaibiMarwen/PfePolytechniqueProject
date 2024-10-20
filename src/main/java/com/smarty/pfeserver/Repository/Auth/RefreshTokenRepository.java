package com.smarty.pfeserver.Repository.Auth;


import com.smarty.pfeserver.Models.Auth.RefreshToken;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(users user);
}
