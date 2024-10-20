package com.smarty.pfeserver.Repository.Country;

import com.smarty.pfeserver.Models.country.City;
import com.smarty.pfeserver.Models.country.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State,Long> {

    List<State> findAllByCities(City city);
    State findTopByNameOrderById(String name);

}
