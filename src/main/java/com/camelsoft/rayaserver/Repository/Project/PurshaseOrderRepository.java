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
    Page<PurshaseOrder> findByArchiveIsFalse(Pageable page);
    Page<PurshaseOrder> findByArchiveIsFalseAndSupplier(Pageable page, Supplier supplier);

    Page<PurshaseOrder> findByArchiveIsFalseAndStatus(Pageable page, PurshaseOrderStatus status);
    Page<PurshaseOrder> findByArchiveIsFalseAndStatusAndSupplier(Pageable page, PurshaseOrderStatus status, Supplier supplier);
    Page<PurshaseOrder> findByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,Date date);
    Page<PurshaseOrder> findByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,Supplier supplier,Date date);
    Page<PurshaseOrder> findByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(Pageable page,Date date, Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(Pageable page, Supplier supplier ,Date date, Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status , Date date, Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndSupplierAndStatusAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(Pageable page,Supplier supplier, PurshaseOrderStatus status , Date date, Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndVehiclesOrderByTimestampDesc(Pageable page,Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndSupplierAndVehiclesOrderByTimestampDesc(Pageable page,Supplier supplier, Vehicles vehicle);
    Page<PurshaseOrder> findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status, Date date);
    Page<PurshaseOrder> findByArchiveIsFalseAndStatusAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(Pageable page,PurshaseOrderStatus status, Supplier supplier, Date date);

    Page<PurshaseOrder> findByStatusAndVehiclesAndArchiveIsFalse(Pageable pageable,  PurshaseOrderStatus status, Vehicles vehicle);
    Page<PurshaseOrder> findByStatusAndSupplierAndVehiclesAndArchiveIsFalse(Pageable pageable,  PurshaseOrderStatus status,Supplier supplier, Vehicles vehicle);



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
