package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.Project.VehiclesPriceFinancing;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles,Long> {
    Page<Vehicles> findAllByArchiveIsFalse(Pageable page);
    Page<Vehicles> findAllByArchiveIsFalseAndSupplier(Pageable page, Supplier supplier);
    List<Vehicles> findAllByArchiveIsFalseAndSupplier(Supplier supplier);
    Page<Vehicles> findAllByArchiveIsFalseAndCarvinContainingIgnoreCaseAndSupplier(Pageable page, String carvin,Supplier supplier);
    Vehicles findTopByCarvin(String vin);
    Vehicles findByCarimages_Id(Long id);
    Vehicles findByVehiclespricefinancing_Id(Long id);
    Vehicles findByCarvin(String carvin);
    @Query("SELECT v FROM Vehicles v "
            + "WHERE v.supplier = :supplier AND v.stock > 0 AND v.archive = false")
    Page<Vehicles> findVehiclesBySupplierAndStockGreaterThanAndArchiveFalse(Pageable pageable, Supplier supplier);

    boolean existsByCarvinAndArchiveIsFalse(String carvin);


}
