package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Project.PurshaseOrderRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }else{
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatus(pg, PurshaseOrderStatus.valueOf(status));
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
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            } else if (status != null && date == null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatus(pg,status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            } else if (status == null && date != null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }else{
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
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
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalse(pg);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatus(pg,status);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date != null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status != null && date == null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findByStatusAndVehiclesAndArchiveIsFalse(pg,status, vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles == null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date != null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg,date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
            else if (status == null && date == null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndVehiclesOrderByTimestampDesc(pg,vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }
           else if (status != null && date != null && vehicles != null) {
                Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg, status , date , vehicles);
                return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
            }

           return null;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


// get po by status ,and date and vehicle and supplier

    public DynamicResponse FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatusAndSupplier(int page, int size, Long idvehecle, PurshaseOrderStatus status , Date date , Long idSupplier ) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            if(idSupplier != null){
                users user = this.userService.findById(idSupplier);
                if(user == null)
                    throw new NotFoundException("User not found");
                Supplier supplier = user.getSupplier();
                if(supplier == null)
                    throw new NotFoundException("Supplier not found");

                if(idvehecle != null) {
                    Vehicles vehicles = this.vehiclesService.FindById(idvehecle);
                    if (vehicles == null)
                        throw new NotFoundException("Vehicle not found");
                    if (status != null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByStatusAndSupplierAndVehiclesAndArchiveIsFalse(pg, status, supplier, vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status == null && date != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg, supplier, date, vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status == null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndSupplierAndVehiclesOrderByTimestampDesc(pg, supplier, vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status != null && date != null && vehicles != null && supplier != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndSupplierAndStatusAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg, supplier, status, date, vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }

                }else {
                    if(status == null && date == null){
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndSupplier(pg, supplier);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status != null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndSupplier(pg,status, supplier);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status != null && date != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,supplier,date);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }  else if (status == null && date != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndSupplierAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,supplier,date);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }

                }


            }else{
                if(idvehecle != null){
                    Vehicles vehicles = this.vehiclesService.FindById(idvehecle);
                    if (vehicles == null)
                        throw new NotFoundException("Vehicle not found");
                    if (status != null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByStatusAndVehiclesAndArchiveIsFalse(pg,status, vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }else if (status == null && date != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg,date , vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status == null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndVehiclesOrderByTimestampDesc(pg,vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status != null && date != null ) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualAndVehiclesOrderByTimestampDesc(pg, status , date , vehicles);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }
                }else{
                    if (status == null && date != null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,date);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }else if (status != null && date != null ) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatusAndTimestampGreaterThanEqualOrderByTimestampDesc(pg,status,date);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    } else if (status != null && date == null) {
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalseAndStatus(pg,status);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }else if(status == null && date == null){
                        Page<PurshaseOrder> pckge = this.repository.findByArchiveIsFalse(pg);
                        return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());
                    }

                }

            }
            return null;

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Integer countPurchaseOrdersWithCustomerOrSupllier(InvoiceRelated related ) {
        if(related == InvoiceRelated.CUSTOMER)
            return this.repository.countByArchiveIsFalseAndCustomerIsNotNull();
        else {
            return this.repository.countByArchiveIsFalseAndSupplierIsNotNull();
        }

    }


    public boolean isTherePoPendingOrInProgressWithCarVin(String carvin){
        List<PurshaseOrderStatus> statusList = Arrays.asList(PurshaseOrderStatus.PENDING, PurshaseOrderStatus.IN_PROGRESS);
        Integer total = this.repository.countPendingOrInProgressPurshaseOrdersByCarvin(statusList, carvin);
        if(total == 0)
            return false;
        else {
            return true;
        }
    }



}
