package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SupplierClassififcationDto {
    private Long id;
    private String name;
    private String description;
    private Set<UserShortDto> suppliers = new HashSet<>();
    private Date timestamp = new Date();
    private Boolean archive = false;

    public static SupplierClassififcationDto supplierClassififcationDtolassToDto(SuppliersClassification classification) {
        SupplierClassififcationDto dto = new SupplierClassififcationDto();
        dto.setId(classification.getId());
        dto.setName(classification.getName());
        dto.setDescription(classification.getDescription());
        dto.setSuppliers(classification.getSuppliers().stream()
                .map(UserShortDto::mapToUserShortDTO).collect(Collectors.toSet()));
        dto.setTimestamp(classification.getTimestamp());
        dto.setArchive(classification.getArchive());
        return dto;
    }
}

