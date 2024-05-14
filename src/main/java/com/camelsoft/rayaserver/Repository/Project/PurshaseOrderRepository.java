package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PurshaseOrderRepository extends JpaRepository<PurshaseOrder, Long> {
    Page<PurshaseOrder> findAllByArchiveIsFalse(Pageable page);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndSupplier(Pageable page, Supplier supplier);

    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatus(Pageable page, PurshaseOrderStatus status);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatusAndSupplier(Pageable page, PurshaseOrderStatus status, Supplier supplier);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,Date date);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(Pageable page,Date date, Vehicles vehicle);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status , Date date, Vehicles vehicle);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndVehiclesContainingOrderByTimestampDesc(Pageable page,Vehicles vehicle);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status, Date date);
    Page<PurshaseOrder> findAllByArchiveIsFalseAndStatusAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status, Supplier supplier, Date date);

    Page<PurshaseOrder> findAllByStatusAndVehiclesContainingAndArchiveIsFalse(Pageable pageable,  PurshaseOrderStatus status, Vehicles vehicle);
    Page<PurshaseOrder> findAllByStatusAndSupplierAndVehiclesContainingAndArchiveIsFalse(Pageable pageable,  PurshaseOrderStatus status,Supplier supplier, Vehicles vehicle);



    @Query("SELECT DISTINCT v FROM Vehicles v " +
            "JOIN v.purchaseOrder po " +
            "WHERE po.supplier = :supplier " +
            "AND po.status = :status " +
            "AND v.archive = false")
    Page<Vehicles> findVehiclesBySupplierAndStatus(Pageable pageable , Supplier supplier, PurshaseOrderStatus status);


    @Query("SELECT DISTINCT v FROM PurshaseOrder po " +
            "JOIN po.vehicles v " +
            "WHERE po.supplier = :supplier")
    Page<Vehicles> findVehiclesBySupplier(Pageable pageable, Supplier supplier);

    @Query("SELECT DISTINCT v FROM Vehicles v " +
            "LEFT JOIN v.purchaseOrder po " +
            "WHERE po.supplier = :supplier " +
            "AND v.carvin LIKE %:vin%")
    Page<Vehicles> findVehiclesBySupplierAndCarvinContaining(Pageable pageable,Supplier supplier, String vin);

    @Query("SELECT DISTINCT v FROM Vehicles v " +
            "JOIN v.purchaseOrder po " +
            "WHERE po.supplier = :supplier " +
            "AND po.status = :status " +
            "AND v.carvin LIKE %:vin% " +
            "AND v.archive = false")
    Page<Vehicles> findVehiclesBySupplierAndStatusAndCarvinContaining(Pageable pageable,Supplier supplier, PurshaseOrderStatus status, String vin);


}
