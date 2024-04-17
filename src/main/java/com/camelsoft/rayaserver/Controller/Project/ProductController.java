package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.Project.Product;
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

import java.io.IOException;
import java.util.List;

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
}
