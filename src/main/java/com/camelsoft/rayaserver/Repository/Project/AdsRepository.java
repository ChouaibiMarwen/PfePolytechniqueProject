package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {
}
