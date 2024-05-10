package com.camelsoft.rayaserver.Controller.Project.AdminStatistics;

import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.Project.ProductService;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/statistics")
public class AdminOverviewStatictics {


    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private LoanServices loanServices;
    @Autowired
    private ProductService productService;


    @GetMapping(value = {"/total_suppliers_count"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> all_suppliers_count() throws IOException {
        Long participation = this.supplierServices.countSuppliers();
        return new ResponseEntity<>(participation, HttpStatus.OK);
    }

}
