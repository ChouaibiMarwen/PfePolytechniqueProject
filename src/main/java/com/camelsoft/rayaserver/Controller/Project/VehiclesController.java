package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.Project.VehiclesPriceFinancing;
import com.camelsoft.rayaserver.Models.User.Supplier;
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

    @PatchMapping(value = {"/update_vehicle/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "update vehicles for supplier", notes = "Endpoint to update vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Vehicles> update_vehicle(@PathVariable Long id_vehicle, @ModelAttribute VehiclesRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        Vehicles vehicles = this.Services.FindById(id_vehicle);
        if (vehicles == null)
            return new ResponseEntity("vehicle " + id_vehicle + " not found in the system", HttpStatus.NOT_FOUND);
        if (vehicles.getSupplier().getId() != supplier.getId())
            return new ResponseEntity("this vehicle " + id_vehicle + " you don't have it", HttpStatus.BAD_REQUEST);
        if (request.getCarmodel() != null) vehicles.setCarmodel(request.getCarmodel());
        if (request.getColor() != null) vehicles.setColor(request.getColor());
        if (request.getCarvin() != null) vehicles.setCarvin(request.getCarvin());
        if (request.getEnginesize() != null) vehicles.setEnginesize(request.getEnginesize());
        if (request.getBodystyle() != null) vehicles.setBodystyle(request.getBodystyle());
        if (request.getInteriorfeatures() != null) vehicles.setInteriorfeatures(request.getInteriorfeatures());
        if (request.getDescription() != null) vehicles.setDescription(request.getDescription());
        if (request.getStock() != null) vehicles.setStock(request.getStock());
        Vehicles result = this.Services.Update(vehicles);
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

    @PatchMapping(value = {"/update_vehicle_price_financing/{id_vehicle_price_financing}"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "update vehicles for supplier", notes = "Endpoint to update vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad request, check the data or the vehicle already have a price please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<VehiclesPriceFinancing> update_vehicle_price_financing(@PathVariable Long id_vehicle_price_financing, @ModelAttribute VehiclesPriceFinancingRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        VehiclesPriceFinancing vehiclesPriceFinancing = this.vehiclesPriceFinancingService.FindById(id_vehicle_price_financing);
        if (vehiclesPriceFinancing == null)
            return new ResponseEntity("vehicle finance" + id_vehicle_price_financing + " not found in the system", HttpStatus.NOT_FOUND);
        if (request.getPrice() != null) vehiclesPriceFinancing.setPrice(request.getPrice());
        if (request.getCurrency() != null) vehiclesPriceFinancing.setCurrency(request.getCurrency());
        if (request.getDiscount() != null) vehiclesPriceFinancing.setDiscount(request.getDiscount());
        if (request.getDiscountpercentage() != null)
            vehiclesPriceFinancing.setDiscountpercentage(request.getDiscountpercentage());
        if (request.getDiscountamount() != null) vehiclesPriceFinancing.setDiscountamount(request.getDiscountamount());
        if (request.getVatpercentage() != null) vehiclesPriceFinancing.setVatpercentage(request.getVatpercentage());
        if (request.getVatamount() != null) vehiclesPriceFinancing.setVatamount(request.getVatamount());
        if (request.getTotalamount() != null) vehiclesPriceFinancing.setTotalamount(request.getTotalamount());
        VehiclesPriceFinancing result = this.vehiclesPriceFinancingService.Update(vehiclesPriceFinancing);
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

    @PatchMapping(value = {"/update_vehicle_media/{id_media}"})
    @PreAuthorize("hasRole('SUPPLIER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data or the vehicle already have a media,or the image please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<VehiclesMedia> update_vehicle_media(@PathVariable Long id_media, @ModelAttribute VehiclesMediaRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        VehiclesMedia vehiclesmedia = this.vehiclesMediaService.FindById(id_media);
        if (vehiclesmedia == null)
            return new ResponseEntity("vehicle media " + id_media + " not found in the system", HttpStatus.NOT_FOUND);
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
                return new ResponseEntity("can't upload rear view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getInteriorviewimage())) {
            interiorviewimage = filesStorageService.save_file(request.getInteriorviewimage(), "vehicles");
            if (interiorviewimage == null) {
                return new ResponseEntity("can't upload interior view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageleft())) {
            sideviewimageleft = filesStorageService.save_file(request.getSideviewimageleft(), "vehicles");
            if (sideviewimageleft == null) {
                return new ResponseEntity("can't upload side view image left", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageright())) {
            sideviewimageright = filesStorageService.save_file(request.getSideviewimageright(), "vehicles");
            if (sideviewimageright == null) {
                return new ResponseEntity("can't upload side view image right", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformatList(request.getAdditionalviewimages())) {
            additionalviewimages = filesStorageService.save_all(request.getAdditionalviewimages(), "vehicles");
            if (additionalviewimages == null || additionalviewimages.isEmpty()) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }

        if (frontviewimage != null) vehiclesmedia.setFrontviewimage(frontviewimage);
        if (rearviewimage != null) vehiclesmedia.setRearviewimage(rearviewimage);
        if (interiorviewimage != null) vehiclesmedia.setInteriorviewimage(interiorviewimage);
        if (sideviewimageleft != null) vehiclesmedia.setSideviewimageleft(sideviewimageleft);
        if (sideviewimageright != null) vehiclesmedia.setSideviewimageright(sideviewimageright);
        if (!additionalviewimages.isEmpty()) vehiclesmedia.setAdditionalviewimages(additionalviewimages);
        VehiclesMedia result = this.vehiclesMediaService.Update(vehiclesmedia);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
