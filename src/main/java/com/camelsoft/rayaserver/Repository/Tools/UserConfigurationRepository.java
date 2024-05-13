package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Tools.UserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, Long> {

}
