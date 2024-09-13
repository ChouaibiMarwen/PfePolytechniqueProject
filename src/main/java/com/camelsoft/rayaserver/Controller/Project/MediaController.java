package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Enum.User.UserActionsEnum;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.UserAction;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Project.VehiclesMedia;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.project.InvoiceMediaRequest;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.camelsoft.rayaserver.Services.Project.VehiclesMediaService;
import com.camelsoft.rayaserver.Services.Project.VehiclesService;
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
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/media")
public class MediaController extends BaseController {
    private final Log logger = LogFactory.getLog(MediaController.class);
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;
    @Autowired
    private UserService userService;

    @Autowired
    private VehiclesService vehiclesService;

    @Autowired
    private VehiclesMediaService vehiclesMediaService;

    @Autowired
    private InvoiceService service;

    @DeleteMapping(value = {"/remove_media/{id_file}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "remove media", notes = "Endpoint to delete media")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the media id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier ,admin or user")
    })
    public ResponseEntity<String> remove_media(@PathVariable Long id_file) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        MediaModel model = this.filesStorageService.findbyid(id_file);
        if (model == null)
            return new ResponseEntity<>("media " + id_file + " not found in the system", HttpStatus.NOT_FOUND);
        this.filesStorageService.delete_file_by_path_from_cdn(model.getUrl(), id_file);

        return new ResponseEntity<>("Media deleted successfully", HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_profile_image"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "update current user profile image", notes = "Endpoint to update profile image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad Request, the media is Null"),
            @ApiResponse(code = 406, message = "Not Acceptable, the type is not acceptable"),
            @ApiResponse(code = 501, message = "Not Implemented, please try again")
    })
    public ResponseEntity<users> update_profile_image(@RequestParam(required = false, name = "file") MultipartFile file) throws IOException {
        users user = this.userService.findByUserName(getCurrentUser().getUsername());
        MediaModel oldimage = user.getProfileimage();
        if (file == null) {
            return new ResponseEntity("file is null", HttpStatus.BAD_REQUEST);
        }
        if (!this.filesStorageService.checkformat(file))
            return new ResponseEntity("this type is not acceptable : ", HttpStatus.NOT_ACCEPTABLE);
        MediaModel resource_media = filesStorageService.save_file_local(file, "profile");
        if (resource_media == null)
            return new ResponseEntity("error saving file", HttpStatus.NOT_IMPLEMENTED);
        user.setProfileimage(resource_media);
        users result = userService.UpdateUser(user);
        if (oldimage != null)
            this.filesStorageService.delete_file_by_path_local(oldimage.getUrl(), oldimage.getId());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_user_profile_image/{userId}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "update user profile image", notes = "Endpoint to update user's profile image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Bad Request, the media is Null"),
            @ApiResponse(code = 406, message = "Not Acceptable, the type is not acceptable"),
            @ApiResponse(code = 501, message = "Not Implemented, please try again")
    })
    public ResponseEntity<users> update_profile_image(@PathVariable Long userId ,@RequestParam(required = false, name = "file") MultipartFile file) throws IOException {
        users user = this.userService.findById(userId);
        if(user == null)
            return new ResponseEntity("user with id: " +userId+ "is not found", HttpStatus.NOT_FOUND);
        MediaModel oldimage = user.getProfileimage();
        if (file == null) {
            return new ResponseEntity("file is null", HttpStatus.BAD_REQUEST);
        }
        if (!this.filesStorageService.checkformat(file))
            return new ResponseEntity("this type is not acceptable : ", HttpStatus.NOT_ACCEPTABLE);
        MediaModel resource_media = filesStorageService.save_file_local(file, "profile");
        if (resource_media == null)
            return new ResponseEntity("error saving file", HttpStatus.NOT_IMPLEMENTED);
        user.setProfileimage(resource_media);
        users result = userService.UpdateUser(user);
        if (oldimage != null)
            this.filesStorageService.delete_file_by_path_local(oldimage.getUrl(), oldimage.getId());
        //save new action
        UserAction action = new UserAction(
                UserActionsEnum.PROFILE_MANAGEMENT,
                user
        );
        this.userActionService.Save(action);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/remove_photo_vehicle/{vehicleId}/{id_photo}"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER') ")
    @ApiOperation(value = "remove vehicle image ", notes = "Endpoint to delete vehicle's image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found, check the media id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier, admin or user")
    })
    public ResponseEntity<String> remove_media_vehicle(@PathVariable Long vehicleId, @PathVariable Long id_photo) {
        try {
            if (id_photo == null) {
                return new ResponseEntity<>("fileId is null", HttpStatus.BAD_REQUEST);
            }

            Vehicles vehicle = this.vehiclesService.FindById(vehicleId);
            if (vehicle == null) {
                return new ResponseEntity<>("Vehicle with id: " + vehicleId + " is not found", HttpStatus.NOT_FOUND);
            }

            VehiclesMedia media = vehicle.getCarimages();
            MediaModel model = null;
            boolean found = false;

            if (media.getFrontviewimage() != null && media.getFrontviewimage().getId().equals(id_photo)) {
                model = media.getFrontviewimage();
                media.setFrontviewimage(null);
                found = true;
            } else if (media.getRearviewimage() != null && media.getRearviewimage().getId().equals(id_photo)) {
                model = media.getRearviewimage();
                media.setRearviewimage(null);
                found = true;
            } else if (media.getInteriorviewimage() != null && media.getInteriorviewimage().getId().equals(id_photo)) {
                model = media.getInteriorviewimage();
                media.setInteriorviewimage(null);
                found = true;
            } else if (media.getSideviewimageleft() != null && media.getSideviewimageleft().getId().equals(id_photo)) {
                model = media.getSideviewimageleft();
                media.setSideviewimageleft(null);
                found = true;
            } else if (media.getSideviewimageright() != null && media.getSideviewimageright().getId().equals(id_photo)) {
                model = media.getSideviewimageright();
                media.setSideviewimageright(null);
                found = true;
            } else {
                Set<MediaModel> additionalViewImages = media.getAdditionalviewimages();
                for (MediaModel addFile : additionalViewImages) {
                    if (addFile.getId().equals(id_photo)) {
                        model = addFile;
                        additionalViewImages.remove(addFile);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return new ResponseEntity<>("No media with that ID belongs to the specified vehicle", HttpStatus.NOT_FOUND);
            }
            this.vehiclesMediaService.Update(media);
            this.filesStorageService.delete_file_by_path_local(model.getUrl(), id_photo);

            return new ResponseEntity<>("Media deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting media: ", e);
            return new ResponseEntity<>("Error deleting media", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = {"/add_invoice_media/{id_invoice}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add invoice attachments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data ,or the image please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Invoice> add_invoice_media(@PathVariable Long id_invoice, @ModelAttribute InvoiceMediaRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Invoice invoice = this.service.FindById(id_invoice);
        if (invoice == null)
            return new ResponseEntity("invoice " + id_invoice + " not found in the system", HttpStatus.NOT_FOUND);


        if (request.getEstimarafile() != null && !request.getEstimarafile().isEmpty()) {
            MediaModel estimarafile = filesStorageService.save_file_local(request.getEstimarafile(), "invoice");
            if (estimarafile == null) {
                return new ResponseEntity("can't upload estimarafile", HttpStatus.BAD_REQUEST);
            }
            invoice.setEstimarafile(estimarafile);
        }

        if (request.getDeliverynotedocument() != null && !request.getDeliverynotedocument().isEmpty()) {
            MediaModel deliverynotedocument = filesStorageService.save_file_local(request.getDeliverynotedocument(), "invoice");
            if (deliverynotedocument == null) {
                return new ResponseEntity("can't upload delivery note document", HttpStatus.BAD_REQUEST);
            }
            invoice.setDeliverynotedocument(deliverynotedocument);
        }

        if (request.getSupplierInvoice() != null && !request.getSupplierInvoice().isEmpty()) {
            MediaModel supplierinvoice = filesStorageService.save_file_local(request.getEstimarafile(), "invoice");
            if (supplierinvoice == null) {
                return new ResponseEntity("can't upload supplierinvoice", HttpStatus.BAD_REQUEST);
            }
            invoice.setSupplierinvoice(supplierinvoice);
        }
        Invoice result = this.service.Update(invoice);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = {"/update_invoice_media/{id_invoice}"})
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    @ApiOperation(value = "add vehicles for supplier", notes = "Endpoint to add invoice attachments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add"),
            @ApiResponse(code = 400, message = "Bad request, check the data ,or the image please use the patch api"),
            @ApiResponse(code = 404, message = "Not found, check the car id"),
            @ApiResponse(code = 403, message = "Forbidden, you are not a supplier")
    })
    public ResponseEntity<Invoice> update_invoice_media(@PathVariable Long id_invoice, @ModelAttribute InvoiceMediaRequest request) throws IOException {
        //users user = UserServices.findByUserName(getCurrentUser().getUsername());
        Invoice invoice = this.service.FindById(id_invoice);
        if (invoice == null)
            return new ResponseEntity("invoice " + id_invoice + " not found in the system", HttpStatus.NOT_FOUND);


        if (request.getEstimarafile() != null && !request.getEstimarafile().isEmpty()) {
            MediaModel estimarafile = filesStorageService.save_file_local(request.getEstimarafile(), "invoice");
            if (estimarafile == null) {
                return new ResponseEntity("can't upload estimarafile", HttpStatus.BAD_REQUEST);
            }
            invoice.setEstimarafile(estimarafile);
        }

        if (request.getDeliverynotedocument() != null && !request.getDeliverynotedocument().isEmpty()) {
            MediaModel deliverynotedocument = filesStorageService.save_file_local(request.getDeliverynotedocument(), "invoice");
            if (deliverynotedocument == null) {
                return new ResponseEntity("can't upload delivery note document", HttpStatus.BAD_REQUEST);
            }
            invoice.setDeliverynotedocument(deliverynotedocument);
        }

        if (request.getSupplierInvoice() != null && !request.getSupplierInvoice().isEmpty()) {
            MediaModel supplierinvoice = filesStorageService.save_file_local(request.getEstimarafile(), "invoice");
            if (supplierinvoice == null) {
                return new ResponseEntity("can't upload supplierinvoice", HttpStatus.BAD_REQUEST);
            }
            invoice.setSupplierinvoice(supplierinvoice);
        }
        Invoice result = this.service.Update(invoice);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
