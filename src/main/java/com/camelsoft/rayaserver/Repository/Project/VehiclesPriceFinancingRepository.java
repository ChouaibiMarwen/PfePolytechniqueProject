package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.VehiclesPriceFinancing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclesPriceFinancingRepository extends JpaRepository<VehiclesPriceFinancing,Long> {
}
