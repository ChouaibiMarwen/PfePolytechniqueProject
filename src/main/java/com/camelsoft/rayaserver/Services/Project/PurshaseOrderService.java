package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Project.PurshaseOrderRepository;
import com.camelsoft.rayaserver.Request.project.PurshaseOrderRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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








}
