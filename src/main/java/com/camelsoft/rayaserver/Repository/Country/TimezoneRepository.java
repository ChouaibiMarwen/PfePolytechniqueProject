package com.camelsoft.rayaserver.Repository.Country;


import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone,Long> {

    List<Timezone> findAllByRoot(Root root);
    boolean existsByZonename(String name);
    Timezone findTopByZonenameOrderById(String name);
}
