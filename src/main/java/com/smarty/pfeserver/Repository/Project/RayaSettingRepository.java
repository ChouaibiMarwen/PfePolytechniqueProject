package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Tools.RayaSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RayaSettingRepository extends JpaRepository<RayaSettings, Long> {
    RayaSettings findFirstByOrderByTimestampDesc();
}
