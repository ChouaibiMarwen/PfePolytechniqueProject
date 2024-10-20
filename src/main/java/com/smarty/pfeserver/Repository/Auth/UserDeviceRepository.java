package com.smarty.pfeserver.Repository.Auth;

import com.smarty.pfeserver.Models.Auth.UserDevice;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice,Long> {
    Optional<UserDevice> findById(Long id);
    Optional<UserDevice> findByToken(String token);
    List<UserDevice> findAllByUser(users user);
    Boolean existsByToken(String token);

    List<UserDevice> findAllByUserOrderByTimestmpDesc(users user);
}
