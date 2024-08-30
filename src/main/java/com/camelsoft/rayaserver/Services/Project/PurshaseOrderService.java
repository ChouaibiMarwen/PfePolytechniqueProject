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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.AvailiabilityEnum;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    public PurshaseOrderService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }
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

  /*  public DynamicResponse FindAllPurchaseOrderPgByVehecleAndDateAndPurchaseOrderStatusAndSupplier(int page, int size, Long idvehecle, PurshaseOrderStatus status , Date date , Long idSupplier ) {
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
*/

    public DynamicResponse findAllPurchaseOrderPgByVehicleAndDateAndPurchaseOrderStatusAndSupplier(int page, int size, Long idVehicle, PurshaseOrderStatus status, Date date, Long idSupplier , users subadmin) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PurshaseOrder> cq = cb.createQuery(PurshaseOrder.class);
            Root<PurshaseOrder> root = cq.from(PurshaseOrder.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("archive")));

            if (idSupplier != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), idSupplier));
            }

            if (idVehicle != null) {
                predicates.add(cb.equal(root.get("vehicles").get("id"), idVehicle));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (date != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), date));
            }

            if (subadmin != null) {
                predicates.add(cb.equal(root.get("subadminassignedto"), subadmin));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(cb.desc(root.get("timestamp")));

            // Use EntityGraph to optimize fetching related entities
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("PurshaseOrder.withDetails");
            TypedQuery<PurshaseOrder> query = entityManager.createQuery(cq);
            query.setHint("javax.persistence.loadgraph", entityGraph);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<PurshaseOrder> resultList = query.getResultList();
            long total = getTotalCountPO(predicates);

            return new DynamicResponse(resultList, pageable.getPageNumber(), total, (int) Math.ceil((double) total / size));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    public DynamicResponse findAllPurchaseOrderPgByVehicleAndDateAndPurchaseOrderStatusAndSupplier(int page, int size, Long idVehicle, PurshaseOrderStatus status, Date date, Supplier supplier , users subadmin) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PurshaseOrder> cq = cb.createQuery(PurshaseOrder.class);
            Root<PurshaseOrder> root = cq.from(PurshaseOrder.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("archive")));

            if (supplier != null) {
                predicates.add(cb.equal(root.get("supplier"), supplier));
            }


            if (idVehicle != null) {
                Join<PurshaseOrder, Vehicles> vehicleJoin = root.join("vehicles");
                Predicate vehicleIdPredicate = cb.equal(vehicleJoin.get("id"), idVehicle);
                Predicate vehicleCarvinPredicate = cb.equal(vehicleJoin.get("carvin"), idVehicle.toString()); // Assuming `idVehicle` can also be a `carvin` value

                // Add predicate to check if either ID or carvin matches
                predicates.add(cb.or(vehicleIdPredicate, vehicleCarvinPredicate));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (date != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), date));
            }

            if (subadmin != null) {
                predicates.add(cb.equal(root.get("subadminassignedto"), subadmin));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(cb.desc(root.get("timestamp")));

            // Use EntityGraph to optimize fetching related entities
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("PurshaseOrder.withDetails");
            TypedQuery<PurshaseOrder> query = entityManager.createQuery(cq);
            query.setHint("javax.persistence.loadgraph", entityGraph);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<PurshaseOrder> resultList = query.getResultList();
            long total = getTotalCountPO(predicates);

            return new DynamicResponse(resultList, pageable.getPageNumber(), total, (int) Math.ceil((double) total / size));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    private long getTotalCountPO(List<Predicate> predicates) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<PurshaseOrder> countRoot = countQuery.from(PurshaseOrder.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
    public Integer countPurchaseOrdersWithCustomerOrSupllier(InvoiceRelated related ) {
        if(related == InvoiceRelated.CUSTOMER)
            return this.repository.countByArchiveIsFalseAndCustomerIsNotNull();
        else {
            return this.repository.countByArchiveIsFalseAndSupplierIsNotNull();
        }

    }


    public Integer countPurchaseOrdersWithSupllier() {
        return this.repository.countByArchiveIsFalseAndSupplierIsNotNull();

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


    public List<PurshaseOrder> getPoListByAssignedSubAdmin(users subadmin){
        return this.repository.findBySubadminassignedtoAndAndArchiveIsFalse(subadmin);
    }



}
