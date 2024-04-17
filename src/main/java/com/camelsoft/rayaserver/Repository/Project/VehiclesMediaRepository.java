package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclesMediaRepository extends JpaRepository<VehiclesMedia,Long> {
}
