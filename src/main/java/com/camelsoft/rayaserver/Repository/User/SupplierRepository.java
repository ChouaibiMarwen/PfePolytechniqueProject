package com.camelsoft.rayaserver.Repository.User;


import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

    @Query("SELECT LOWER(u.city), COUNT(s) FROM Supplier s JOIN s.user u GROUP BY LOWER(u.city)")
    List<Object[]> countSuppliersByCity();





}
