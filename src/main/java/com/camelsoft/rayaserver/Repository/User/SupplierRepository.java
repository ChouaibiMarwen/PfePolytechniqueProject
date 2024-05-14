package com.camelsoft.rayaserver.Repository.User;


import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

    @NotNull
    Page<Supplier> findAll(@NotNull Pageable pageable);

   /* @Query("SELECT DISTINCT u FROM Supplier s " +
            "JOIN s.user u " +
            "JOIN u.purchaseOrders po " +
            "WHERE (:name IS NULL OR CONCAT(u.personalinformation.firstnameen, ' ', u.personalinformation.lastnameen) LIKE %:name%) " +
            "AND po.status = :status " +
            "AND u.active = true")
    Page<users> findUsersByPurchaseOrderStatusAndName(Pageable pageable, String name, PurshaseOrderStatus status);*/
   @Query("SELECT DISTINCT u FROM Supplier s " +
           "JOIN s.user u " +
           "JOIN u.personalinformation pi " +
           "JOIN u.purchaseOrders po " +
           "WHERE (:name IS NULL OR CONCAT(pi.firstnameen, ' ', pi.lastnameen) LIKE %:name%) " +
           "AND (:status IS NULL OR po.status = :status) " +
           "AND u.active = true")
   Page<users> findUsersByNameAndPurchaseOrderStatus(Pageable pageable,String name, PurshaseOrderStatus status);
   // Page<users> findAll(@NotNull Pageable pageable);

    @Query("SELECT DISTINCT u FROM Supplu.personalinformation.firstnameenier s " +
            "JOIN s.user u " +
            "JOIN u.purchaseOrders po " +
            "WHERE po.status = :status " +
            "AND u.active = true")
    Page<users> findUsersByPurchaseOrderStatus(Pageable pageable, PurshaseOrderStatus status);



    @Query("SELECT DISTINCT u FROM Supplier s " +
            "JOIN s.user u " +
            "JOIN u.personalinformation pi " +
            "WHERE (:name IS NULL OR CONCAT(pi.firstnameen, ' ', pi.lastnameen) LIKE %:name%) " +
            "AND u.active = true")
    Page<users> findUsersByNameContaining(Pageable pageable, String name);




}
