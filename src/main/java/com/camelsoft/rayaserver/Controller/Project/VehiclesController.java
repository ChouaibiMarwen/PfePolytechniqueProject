package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.Project.VehiclesPriceFinancing;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.VehiclesMediaRequest;
import com.camelsoft.rayaserver.Request.project.VehiclesPriceFinancingRequest;
import com.camelsoft.rayaserver.Request.project.VehiclesRequest;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.LoanServices;
import com.camelsoft.rayaserver.Services.Project.VehiclesMediaService;
import com.camelsoft.rayaserver.Services.Project.VehiclesPriceFinancingService;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
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
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/vehicles")
public class VehiclesController extends BaseController {
    private final Log logger = LogFactory.getLog(VehiclesController.class);
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");
    @Autowired
    private VehiclesService Services;
    @Autowired
    private VehiclesMediaService vehiclesMediaService;
    @Autowired
    private VehiclesPriceFinancingService vehiclesPriceFinancingService;
    @Autowired
    private UserService UserServices;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @GetMapping(value = {"/all_vehicles_admin"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all vehicles for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        // users user = UserServices.findByUserName(getCurrentUser().getUsername());
        DynamicResponse result = this.Services.FindAllPg(page, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_vehicles_supplier"})
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all vehicles for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the supplier")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        DynamicResponse result = this.Services.FindAllPgSupplier(page, size, user.getSupplier());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_vehicle"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Vehicles> add_vehicle(@ModelAttribute VehiclesRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Vehicles model = new Vehicles(
                request.getCarmodel(),
                request.getColor(),
                request.getCarvin(),
                request.getEnginesize(),
                request.getFueltype(),
                request.getBodystyle(),
                request.getExteriorfeatures(),
                request.getInteriorfeatures(),
                request.getDescription(),
                request.getStock(),
                user.getSupplier()
        );
        Vehicles result = this.Services.Save(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_vehicle_price_financing/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data or the vehicle already have a price please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<VehiclesPriceFinancing> add_vehicle_price_financing(@PathVariable Long id_vehicle, @ModelAttribute VehiclesPriceFinancingRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Vehicles vehicles = this.Services.FindById(id_vehicle);
        if (vehicles == null)
            return new ResponseEntity("vehicle " + id_vehicle + " not found in the system", HttpStatus.NOT_FOUND);
        if (vehicles.getVehiclespricefinancing() != null)
            return new ResponseEntity("this vehicle " + id_vehicle + " already have price and financing", HttpStatus.BAD_REQUEST);

        VehiclesPriceFinancing model = new VehiclesPriceFinancing(
                request.getPrice(),
                request.getCurrency(),
                request.getDiscount(),
                request.getDiscountpercentage(),
                request.getDiscountamount(),
                request.getVatpercentage(),
                request.getVatamount(),
                request.getTotalamount()
        );
        VehiclesPriceFinancing result = this.vehiclesPriceFinancingService.Save(model);
        vehicles.setVehiclespricefinancing(result);
        this.Services.Update(vehicles);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_vehicle_media/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data or the vehicle already have a media,or the image please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<VehiclesMedia> add_vehicle_media(@PathVariable Long id_vehicle, @ModelAttribute VehiclesMediaRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Vehicles vehicles = this.Services.FindById(id_vehicle);
        if (vehicles == null)
            return new ResponseEntity("vehicle " + id_vehicle + " not found in the system", HttpStatus.NOT_FOUND);
        if (vehicles.getVehiclespricefinancing() != null)
            return new ResponseEntity("this vehicle " + id_vehicle + " already have media", HttpStatus.BAD_REQUEST);
        File_model frontviewimage = null;
        File_model rearviewimage = null;
        File_model interiorviewimage = null;
        File_model sideviewimageleft = null;
        File_model sideviewimageright = null;
        Set<File_model> additionalviewimages = new HashSet<>();
        if (this.filesStorageService.checkformat(request.getFrontviewimage())) {
            frontviewimage = filesStorageService.save_file(request.getFrontviewimage(), "vehicles");
            if (frontviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getRearviewimage())) {
            rearviewimage = filesStorageService.save_file(request.getRearviewimage(), "vehicles");
            if (rearviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getInteriorviewimage())) {
            interiorviewimage = filesStorageService.save_file(request.getInteriorviewimage(), "vehicles");
            if (interiorviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageleft())) {
            sideviewimageleft = filesStorageService.save_file(request.getSideviewimageleft(), "vehicles");
            if (sideviewimageleft == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageright())) {
            sideviewimageright = filesStorageService.save_file(request.getSideviewimageright(), "vehicles");
            if (sideviewimageright == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformatList(request.getAdditionalviewimages())) {
            additionalviewimages = filesStorageService.save_all(request.getAdditionalviewimages(), "vehicles");
            if (additionalviewimages == null || additionalviewimages.isEmpty()) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }

        VehiclesMedia model = new VehiclesMedia(
                frontviewimage,
                rearviewimage,
                interiorviewimage,
                sideviewimageleft,
                sideviewimageright,
                additionalviewimages
        );
        VehiclesMedia result = this.vehiclesMediaService.Save(model);
        vehicles.setCarimages(result);
        this.Services.Update(vehicles);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
