package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.AvailiabilityEnum;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.ConditionEnum;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.TransmissionTypeEnum;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.VehiclesPostStatus;
import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.UserAction;
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
import com.camelsoft.rayaserver.Services.Project.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/vehicles")
public class VehiclesController extends BaseController {
    private final Log logger = LogFactory.getLog(VehiclesController.class);

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private VehiclesService Services;
    @Autowired
    private VehiclesMediaService vehiclesMediaService;
    @Autowired
    private VehiclesPriceFinancingService vehiclesPriceFinancingService;
    @Autowired
    private UserService UserServices;
    @Autowired
    private PurshaseOrderService purshaseOrderService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @GetMapping(value = {"/all_vehicles_admin"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get all vehicles for admin", notes = "Endpoint to get vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the admin")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_admin(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        DynamicResponse result = this.Services.FindAllPg(page, size);
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_vehicles_supplier"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all vehicles for  supplier ", notes = "Endpoint to get vehicles for supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the supplier")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_supplier(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        DynamicResponse result = this.Services.FindAllPgSupplier(page, size, user.getSupplier());
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/all_vehicles_by_supplier/{idSupplier}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all vehicles by supplier for admin", notes = "Endpoint to get a supllier's vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the supplier")
    })
    public ResponseEntity<DynamicResponse> all_vehicles_by_supplier(@PathVariable Long idSupplier ,@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size) throws IOException {
        users user = UserServices.findById(idSupplier);
        if (user == null)
            return new ResponseEntity(" supplier not found or null in the system", HttpStatus.NOT_FOUND);
        if (user.getSupplier() == null)
            return new ResponseEntity(" id provided is not for a supplier", HttpStatus.NOT_FOUND);

        DynamicResponse result = this.Services.FindAllPgSupplier(page, size, user.getSupplier());
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/all_vehicles_by_supplier_and_vin_and_purchase_order_status/{idSupplier}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get all vehicles by supplier for admin", notes = "Endpoint to get a supllier's vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
            @ApiResponse(code = 400, message = "Bad request, check the status , page or size"),
            @ApiResponse(code = 403, message = "Forbidden, you are not the supplier")
    })
    public ResponseEntity<DynamicResponse> allvehiclesbysupplierAndVinAndPoStatus(@PathVariable Long idSupplier ,@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int size, @RequestParam(required = false) String vin, @RequestParam(required = false) PurshaseOrderStatus status) throws IOException {
        users user = UserServices.findById(idSupplier);
        if (user == null)
            return new ResponseEntity(" supplier not found or null in the system", HttpStatus.NOT_FOUND);
        if (user.getSupplier() == null)
            return new ResponseEntity(" id provided is not for a supplier", HttpStatus.NOT_FOUND);

        // DynamicResponse result = this.Services.FindAllPgSupplier(page, size, user.getSupplier());
       DynamicResponse result = this.purshaseOrderService.FindAllVehiclesPgBySupplierAndCarvinAndPurchaseOrderStatus(page, size, user.getSupplier(), vin, status);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                currentuser
        );
        this.userActionService.Save(action);
       return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(value = {"/add_vehicle"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')" )
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add vehicles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Vehicles> add_vehicle(@ModelAttribute VehiclesRequest request) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Supplier supplier = user.getSupplier();
        if (supplier == null)
            return new ResponseEntity(" supplier not found or null in the system", HttpStatus.NOT_FOUND);

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
                supplier,
                request.getCarmake(),
                request.getMileage(),
                request.getYear(),
                request.getDoors(),
                request.getAvailiability(),
                request.getCondition(),
                request.getTransmissiontype()

        );
        Vehicles result = this.Services.Save(model);


        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_vehicle/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
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
        if(request.getCarmake() != null) vehicles.setCarmake(request.getCarmake());
        if(request.getYear() != null) vehicles.setYear(request.getYear());
        if(request.getDoors() != null) vehicles.setDoors(request.getDoors());
        if(request.getAvailiability() != null) vehicles.setAvailiability(request.getAvailiability());
        if(request.getCondition() != null) vehicles.setCondition(request.getCondition());
        if(request.getTransmissiontype() != null) vehicles.setTransmissiontype(request.getTransmissiontype());


        Vehicles result = this.Services.Update(vehicles);
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id_vehicle}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "get vehicles by id for supplier", notes = "Endpoint get vehicles by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Vehicles> get_vehicle_by_id(@PathVariable Long id_vehicle) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (!this.Services.ExistById(id_vehicle))
            return new ResponseEntity("vehicle " + id_vehicle + " not found in the system", HttpStatus.NOT_FOUND);
        Vehicles result = this.Services.FindById(id_vehicle);
        if (user.getSupplier() != null) {
            Supplier supplier = user.getSupplier();
            if (result.getSupplier().getId() != supplier.getId())
                return new ResponseEntity("this vehicle " + id_vehicle + " you don't have it", HttpStatus.BAD_REQUEST);

        }

        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                user
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = {"/vehecles_available_stock_by_supplier/{idSupplier} "})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
    @ApiOperation(value = "get vehecles with available stock by supplier for admin ", notes = "vehecles with available stock by supplier for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<DynamicResponse> vehecles_available_stock_by_supplier(@PathVariable Long idSupplier, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
       // users user = UserServices.findByUserName(getCurrentUser().getUsername());
        users user =  this.UserServices.findById(idSupplier);
        if(user == null)
            return new ResponseEntity("user " + idSupplier + " not found in the system", HttpStatus.NOT_FOUND);
        Supplier supplier = user.getSupplier();
        if(supplier == null)
            return new ResponseEntity("supplier not found in the system", HttpStatus.NOT_FOUND);
        DynamicResponse result =  this.Services.findAllPgBySupplierAndAvailableStock(page, size, supplier);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.SUPPLIER_VEHICLES_READ,
                currentuser
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @PostMapping(value = {"/add_vehicle_price_financing/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
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
        if (vehicles.getCarimages() != null) {
            vehicles.setStatus(VehiclesPostStatus.PUBLISHED);
        }
        this.Services.Update(vehicles);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_vehicle_price_financing/{id_vehicle_price_financing}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
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
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_vehicle_media/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
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
            frontviewimage = filesStorageService.save_file_local(request.getFrontviewimage(), "vehicles");
            if (frontviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getRearviewimage())) {
            rearviewimage = filesStorageService.save_file_local(request.getRearviewimage(), "vehicles");
            if (rearviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getInteriorviewimage())) {
            interiorviewimage = filesStorageService.save_file_local(request.getInteriorviewimage(), "vehicles");
            if (interiorviewimage == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageleft())) {
            sideviewimageleft = filesStorageService.save_file_local(request.getSideviewimageleft(), "vehicles");
            if (sideviewimageleft == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformat(request.getSideviewimageright())) {
            sideviewimageright = filesStorageService.save_file_local(request.getSideviewimageright(), "vehicles");
            if (sideviewimageright == null) {
                return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
            }
        }
        if (this.filesStorageService.checkformatArrayList(request.getAdditionalviewimages())) {
            List<MultipartFile> fileList = new ArrayList<>(request.getAdditionalviewimages());
            additionalviewimages = filesStorageService.save_all_local(fileList, "vehicles");
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
        if (vehicles.getVehiclespricefinancing() != null) {
            vehicles.setStatus(VehiclesPostStatus.PUBLISHED);
        }
        this.Services.Update(vehicles);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_vehicle_media/{id_media}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
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

        if(request.getFrontviewimage() != null && !request.getFrontviewimage().isEmpty()){
            if (this.filesStorageService.checkformat(request.getFrontviewimage())) {
                frontviewimage = filesStorageService.save_file_local(request.getFrontviewimage(), "vehicles");
                if (frontviewimage == null) {
                    return new ResponseEntity("can't upload front view image", HttpStatus.BAD_REQUEST);
                }
                File_model media = vehiclesmedia.getFrontviewimage();
                vehiclesmedia.setFrontviewimage(null);
                this.filesStorageService.delete_file_by_path_local(media.getUrl(), media.getId());
                vehiclesmedia.setFrontviewimage(frontviewimage);
                this.vehiclesMediaService.Update(vehiclesmedia);
            }
        }

        if(request.getRearviewimage() != null  && !request.getRearviewimage().isEmpty()){
            if (this.filesStorageService.checkformat(request.getRearviewimage())) {
                rearviewimage = filesStorageService.save_file_local(request.getRearviewimage(), "vehicles");
                if (rearviewimage == null) {
                    return new ResponseEntity("can't upload rear view image", HttpStatus.BAD_REQUEST);
                }
            }
        }

        if(request.getInteriorviewimage() != null  && !request.getInteriorviewimage().isEmpty()){
            if (this.filesStorageService.checkformat(request.getInteriorviewimage())) {
                interiorviewimage = filesStorageService.save_file_local(request.getInteriorviewimage(), "vehicles");
                if (interiorviewimage == null) {
                    return new ResponseEntity("can't upload interior view image", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if(request.getSideviewimageleft() != null  && !request.getSideviewimageleft().isEmpty()){
            if (this.filesStorageService.checkformat(request.getSideviewimageleft())) {
                sideviewimageleft = filesStorageService.save_file_local(request.getSideviewimageleft(), "vehicles");
                if (sideviewimageleft == null) {
                    return new ResponseEntity("can't upload side view image left", HttpStatus.BAD_REQUEST);
                }
            }
        }

        if(request.getSideviewimageright() != null  && !request.getSideviewimageright().isEmpty()){
            if (this.filesStorageService.checkformat(request.getSideviewimageright())) {
                sideviewimageright = filesStorageService.save_file_local(request.getSideviewimageright(), "vehicles");
                if (sideviewimageright == null) {
                    return new ResponseEntity("can't upload side view image right", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if(request.getAdditionalviewimages() != null  && !request.getAdditionalviewimages().isEmpty()){
            if (this.filesStorageService.checkformatArrayList(request.getAdditionalviewimages())) {
                additionalviewimages = filesStorageService.save_all_local(request.getAdditionalviewimages(), "vehicles");
                if (additionalviewimages == null || additionalviewimages.isEmpty()) {
                    return new ResponseEntity("can't upload additional view image", HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (frontviewimage != null) vehiclesmedia.setFrontviewimage(frontviewimage);
        if (rearviewimage != null) vehiclesmedia.setRearviewimage(rearviewimage);
        if (interiorviewimage != null) vehiclesmedia.setInteriorviewimage(interiorviewimage);
        if (sideviewimageleft != null) vehiclesmedia.setSideviewimageleft(sideviewimageleft);
        if (sideviewimageright != null) vehiclesmedia.setSideviewimageright(sideviewimageright);
        if (!additionalviewimages.isEmpty()) vehiclesmedia.setAdditionalviewimages(additionalviewimages);
        VehiclesMedia result = this.vehiclesMediaService.Update(vehiclesmedia);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }





    @PatchMapping(value = {"delete_vehicle/{id_vehicle}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "delete vehicle for supplier", notes = "Endpoint delete vehicles by supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 400, message = "Bad request, check the data"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier or sub_supplier")
    })
    public ResponseEntity<Vehicles> delete_vehicle(@PathVariable Long id_vehicle) throws IOException {
        users user = UserServices.findByUserName(getCurrentUser().getUsername());
        if (!this.Services.ExistById(id_vehicle))
            return new ResponseEntity("vehicle " + id_vehicle + " not found in the system", HttpStatus.NOT_FOUND);
        Vehicles result = this.Services.FindById(id_vehicle);
        if (user.getSupplier() != null) {
            Supplier supplier = user.getSupplier();
            if (result.getSupplier().getId() != supplier.getId())
                return new ResponseEntity("this vehicle " + id_vehicle + " you don't have it", HttpStatus.BAD_REQUEST);

        }
        result.setArchive(true);
        result = this.Services.Update(result);
        users currentuser = UserServices.findByUserName(getCurrentUser().getUsername());
        //
        UserAction action = new UserAction(
                UserActionsEnum.VEHICLES_MANAGEMENT,
                currentuser
        );
        this.userActionService.Save(action);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = {"/remove_media_vehicle/{vehicleId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "remove vehicle media", notes = "Endpoint to delete vehicle's media")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the media id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier, admin or user")
    })
    public ResponseEntity<Vehicles> remove_media_vehicle(@PathVariable Long vehicleId) {
        Vehicles vehicle = this.Services.FindById(vehicleId);
        if (vehicle == null) {
            return new ResponseEntity("Vehicle with id: " + vehicleId + " is not found", HttpStatus.NOT_FOUND);
        }
       // VehiclesMedia media = vehicle.getCarimages();
        vehicle.setCarimages(null);
       Vehicles result = this.Services.Update(vehicle);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}


