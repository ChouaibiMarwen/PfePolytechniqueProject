package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.DTO.PurchaseOrderDto;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Request.project.EventRequest;
import com.camelsoft.rayaserver.Request.project.ProductRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Project.ProductService;
import com.camelsoft.rayaserver.Services.User.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/product")
public class ProductController {
    private final Log logger = LogFactory.getLog(ProductController.class);

    @Autowired
    private ProductService service;
    @Autowired
    private UserService UserServices;

    @GetMapping(value = {"/all_product_by_name"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all product for admin by name", notes = "Endpoint to get product by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Product>> all_product_by_name(@ModelAttribute String name) throws IOException {
        List<Product> result = this.service.findAllByName(name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/add_product")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a new product from the admin", notes = "Endpoint to add a new event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added product "),
            @ApiResponse(code = 400, message = "Bad request, check request credentials "),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Product> addProduct(@ModelAttribute ProductRequest request) throws IOException {
        if (request.getName() == null || request.getName().equals(""))
            return new ResponseEntity("Name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (request.getQuantity() == null || request.getQuantity() == 0D)
            return new ResponseEntity("Quantity can't be null or equals to 0", HttpStatus.BAD_REQUEST);
        if (request.getUnitprice() == null || request.getUnitprice() == 0D)
            return new ResponseEntity("Sent to list can't be null or equals to 0", HttpStatus.BAD_REQUEST);
        Product  product  = new Product(
                request.getName(),
                request.getQuantity(),
                request.getUnitprice(),
                request.getTaxespercentage(),
                 request.getDiscountpercentage(),
                 request.getSubtotal()
        );
        Product result = this.service.Save(product);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/product/{id]"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get product by id for admin ", notes = "Endpoint to get product by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Product> getProductById(@PathVariable(required = false) Long id) throws IOException {
        Product result = this.service.FindById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
