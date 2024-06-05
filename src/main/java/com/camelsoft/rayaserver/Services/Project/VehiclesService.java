package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.Vehicles;

import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.Project.VehiclesPriceFinancing;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Repository.Project.VehiclesRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehiclesService {
    @Autowired
    private VehiclesRepository repository;


    public Vehicles Save(Vehicles model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Vehicles Update(Vehicles model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Vehicles FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }
  public Vehicles FindByVehiclesmediaid(Long id) {
        try {
                return this.repository.findByCarimages_Id(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Vehicles FindByfindByehiclespricefinancingid(Long id) {
        try {
            return this.repository.findByVehiclespricefinancing_Id(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


  public Vehicles FindByVIN(String vin) {
        try {

            return this.repository.findTopByCarvin(vin);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllPg(int page, int size) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Vehicles> pckge = this.repository.findAllByArchiveIsFalse(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
public DynamicResponse FindAllPgSupplier(int page, int size, Supplier supplier) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Vehicles> pckge = this.repository.findAllByArchiveIsFalseAndSupplier(pg,supplier);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
public List<Vehicles> FindAllSupplier( Supplier supplier) {
        try {
            return this.repository.findAllByArchiveIsFalseAndSupplier(supplier);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllPgByCarVinSupplier(int page, int size, String carvin ,Supplier supplier) {
        try {

            PageRequest pg = PageRequest.of(page, size);
            Page<Vehicles> pckge = this.repository.findAllByArchiveIsFalseAndCarvinContainingIgnoreCaseAndSupplier(pg,carvin ,supplier);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse findAllPgBySupplierAndAvailableStock(int page, int size ,Supplier supplier) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Vehicles> pckge = this.repository.findVehiclesBySupplierAndStockGreaterThanAndArchiveFalse(pg,supplier);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<Vehicles> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public boolean ExistById(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }




    public void DeleteById(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public boolean inStock( Vehicles vehicles, Integer demandedQuantity ){
        return vehicles.getStock() - demandedQuantity >= 0;
    }

   /* public Vehicles getVeheclesByPriceFinancing(VehiclesPriceFinancing vehiclesPriceFinancing) {
        return this.repository.findByVehiclespricefinancing(vehiclesPriceFinancing);
    }

    public Vehicles getVeheclesByCarImage(VehiclesMedia vehiclesMedia) {
        return this.repository.findByCarimages(vehiclesMedia);
    }

*/


}
