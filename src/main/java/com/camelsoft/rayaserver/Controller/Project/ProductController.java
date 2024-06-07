package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.ProductRequest;
import com.camelsoft.rayaserver.Services.Project.ProductService;
import com.camelsoft.rayaserver.Services.User.UserActionService;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
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
public class ProductController extends BaseController {
    private final Log logger = LogFactory.getLog(ProductController.class);

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private ProductService service;
    @Autowired
    private UserService userService;

    @GetMapping(value = {"/all_product_by_name"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all product for admin by name", notes = "Endpoint to get product by name and character")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<List<Product>> all_product_by_name(@ModelAttribute String name) throws IOException {
        List<Product> result = this.service.findAllByName(name);
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PRODUCT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/add_product")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "Add a new product from the admin", notes = "Endpoint to add a new event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added product "),
            @ApiResponse(code = 400, message = "Bad request, check request credentials "),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Product> addProduct(@ModelAttribute ProductRequest request) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
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
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PRODUCT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/product/{id]"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get product by id for admin ", notes = "Endpoint to get product by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<Product> getProductById(@PathVariable(required = false) Long id) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        Product result = this.service.FindById(id);
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PRODUCT_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
