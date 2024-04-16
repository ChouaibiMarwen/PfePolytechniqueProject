package com.camelsoft.rayaserver.Repository.Tools;


import com.camelsoft.rayaserver.Models.Tools.Rating;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {



    Boolean existsByUserAndSupplier(users user, Supplier product);

    @Query("SELECT SUM(r.rating) FROM Rating r WHERE r.supplier is NOT null AND r.rating > 3")
    Long getSumOfPositiveRatingsForSupplier();

    @Query("SELECT SUM(r.rating) FROM Rating r WHERE r.supplier is NOT null AND r.rating < 3")
    Long getSumOfBadRatingsForSupplier();




    Page<Rating> findAllBySupplierOrderByTimestampDesc(Pageable page, Supplier supplier);

}
