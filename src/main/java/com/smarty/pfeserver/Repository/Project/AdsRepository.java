package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {
}
