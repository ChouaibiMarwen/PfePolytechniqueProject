package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
     
}
