package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Repository.Project.PurshaseOrderRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class PurshaseOrderService {

    @Autowired
    private PurshaseOrderRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private VehiclesService vehiclesService;

    public PurshaseOrder Save(PurshaseOrder model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
    public PurshaseOrder Update(PurshaseOrder model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public PurshaseOrder FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No Purshase order found with id in our data base", id));
        }

    }

    public DynamicResponse findAllPgByStatus(int page, int size, String status) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            if(status == null || status.isEmpty()){
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }else{
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatus(pg, PurshaseOrderStatus.valueOf(status));
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }


        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse findAllPgByStatusAndDate(int page, int size, PurshaseOrderStatus status, Date date) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            if(status == null && date == null){
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            } else if (status != null && date == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatus(pg,status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            } else if (status == null && date != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }else{
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }


        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllVehiclesPgBySupplierAndCarvinAndPurchaseOrderStatus(int page, int size, Supplier supplier, String carvin , PurshaseOrderStatus status) {
        try {

            if(status == null && carvin == null){
                return this.vehiclesService.FindAllPgSupplier(page, size, supplier);
            }else if (status != null && carvin == null ){
                PageRequest pg = PageRequest.of(page, size);
                Page<Vehicles> pckge = this.repository.findVehiclesBySupplierAndStatus(pg,supplier, status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }else if(status == null && carvin != null){
                return this.vehiclesService.FindAllPgByCarVinSupplier(page,size,carvin,supplier);
            }else{
                PageRequest pg = PageRequest.of(page, size);
                Page<Vehicles> pckge = this.repository.findVehiclesBySupplierAndStatusAndCarvinContaining(pg,supplier, status, carvin);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

// get po by status ,and date and vehicle

    public DynamicResponse FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatus(int page, int size, Vehicles vehicles, PurshaseOrderStatus status , Date date) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            if(status == null && date == null && vehicles == null){
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatus(pg,status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByStatusAndVehiclesContainingAndArchiveIsFalse(pg,status, vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg,date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date == null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndVehiclesContainingOrderByTimestampDesc(pg,vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
           else if (status != null && date != null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg, status , date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }

           return null;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


// get po by status ,and date and vehicle and supplier

    public DynamicResponse FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatusAndSupplier(int page, int size, Vehicles vehicles, PurshaseOrderStatus status , Date date , Supplier supplier) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            if(status == null && date == null && vehicles == null&&supplier == null){
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if(status == null && date == null && vehicles == null&&supplier != null){
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndSupplier(pg, supplier);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles == null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatus(pg,status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles == null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndSupplier(pg,status, supplier);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles == null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles == null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,supplier,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles != null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByStatusAndVehiclesContainingAndArchiveIsFalse(pg,status, vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles != null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByStatusAndSupplierAndVehiclesContainingAndArchiveIsFalse(pg,status, supplier , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles == null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles == null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,supplier,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles != null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg,date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles != null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg,supplier , date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date == null && vehicles != null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndVehiclesContainingOrderByTimestampDesc(pg,vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date == null && vehicles != null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndSupplierAndVehiclesContainingOrderByTimestampDesc(pg,supplier, vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles != null && supplier == null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg, status , date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles != null && supplier != null) {
                Page<PurshaseOrder> pckge = this.repository.findAllByArchiveIsFalseAndSupplierAndStatusAndTimestampGreaterThanEqualAndVehiclesContainingOrderByTimestampDesc(pg,supplier, status , date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }


            return null;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }





}
