package com.camelsoft.rayaserver.Repository.Auth;


import com.camelsoft.rayaserver.Models.Auth.RefreshToken;
import com.camelsoft.rayaserver.Models.Auth.users;
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
