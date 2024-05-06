package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.PurshaseOrder.PurshaseOrderStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class PurshaseOrderRequest {
    private Date orderDate;
    private PurshaseOrderStatus status;
    private Long supplierId;
    private Long vehicleId;
    private Integer quantity;
    private Double discountamount;
    private Date requestDeliveryDate;
    private String streetAddress;
    private String city;
    private String state;
    private String codePostal;
    private String country;
    private String description;
    private Set<MultipartFile> attachments = new HashSet<>();
}
