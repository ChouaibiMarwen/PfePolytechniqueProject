package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Service_Agreement;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import lombok.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class PurchaseOrderDto {
        private Long id;
        private Date orderDate;
        private PurshaseOrderStatus status;
        private SupplierDto supplier;
        private Vehicles vehicle;
        private Integer quantity;
        private Double discountamount;
        private Date requestDeliveryDate;
        private String streetAddress;
        private String city;
        private String state;
        private String codePostal;
        private String country;
        private String description;
        private Set<MediaModel> attachments = new HashSet<>();
        private Boolean archive = false;
        private Integer poCountBySupplier;
        private Set<Service_Agreement> serviceagreements = new HashSet<>();
        private Date timestamp;



        public static PurchaseOrderDto PurchaseOrderToDto(PurshaseOrder purchaseOrder) {
                PurchaseOrderDto dto = new PurchaseOrderDto();
                dto.setId(purchaseOrder.getId());
                dto.setOrderDate(purchaseOrder.getOrderDate());
                dto.setStatus(purchaseOrder.getStatus());
                dto.setSupplier(SupplierDto.mapToUserShortDTO(purchaseOrder.getSupplier()));
                dto.setVehicle(purchaseOrder.getVehicles());
                dto.setQuantity(purchaseOrder.getQuantity());
                dto.setDiscountamount(purchaseOrder.getDiscountamount());
                dto.setRequestDeliveryDate(purchaseOrder.getRequestDeliveryDate());
                dto.setStreetAddress(purchaseOrder.getStreetAddress());
                dto.setCity(purchaseOrder.getCity());
                dto.setState(purchaseOrder.getState());
                dto.setCodePostal(purchaseOrder.getCodePostal());
                dto.setCountry(purchaseOrder.getCountry());
                dto.setDescription(purchaseOrder.getDescription());
                dto.setAttachments(purchaseOrder.getAttachments());
                dto.setArchive(purchaseOrder.getArchive());
                dto.setTimestamp(purchaseOrder.getTimestamp());
                dto.setPoCountBySupplier(purchaseOrder.getPoCountBySupplier());
                return dto;
        }






    }
