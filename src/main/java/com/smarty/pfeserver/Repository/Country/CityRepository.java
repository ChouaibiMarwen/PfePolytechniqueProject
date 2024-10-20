package com.smarty.pfeserver.Repository.Country;

import com.smarty.pfeserver.Models.country.City;
import com.smarty.pfeserver.Models.country.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {

    List<City> findAllByState(State state);
    City findByName(String name);
}
