package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Repository.Project.ProductRepository;
import com.camelsoft.rayaserver.Repository.Project.ProductRepository;
import com.camelsoft.rayaserver.Request.project.ProductRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;


    public Product Save(Product model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
  public Set<Product> SaveProductList(Set<ProductRequest> models) {
        try {
            Set<Product> products = new HashSet<>();
            for (ProductRequest req: models) {
                if(req.getId() !=null) {
                    Product res = this.FindById(req.getId());
                    if(res==null)
                        continue;
                    products.add(res);
                    continue;
                }
                Product product = new Product(
                        req.getName(),
                        req.getQuantity(),
                        req.getUnitprice(),
                        req.getTaxespercentage(),
                        req.getDiscountpercentage(),
                        req.getSubtotal()
                );
                Product result = this.Save(product);
                products.add(result);
            }
            return products;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Product Update(Product model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Product FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse FindAllPg(int page, int size) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Product> pckge = this.repository.findAll(pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<Product> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<Product> findAllByName(String name) {
        try {
            return this.repository.findAllByNameContainingIgnoreCaseAndArchiveIsFalse(name);
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
}
