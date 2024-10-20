package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
