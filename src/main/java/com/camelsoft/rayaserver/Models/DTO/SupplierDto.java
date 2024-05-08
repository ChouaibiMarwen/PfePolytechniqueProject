package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;

@Data
public class SupplierDto {
    private Long id;
    private String name;
    private String pic;
    private String email;

    public static SupplierDto mapToUserShortDTO(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getUserId());

        PersonalInformation personalInformation = supplier.getUser().getPersonalinformation();
        if (personalInformation != null) {
            String firstName = personalInformation.getFirstnameen();
            String lastName = personalInformation.getLastnameen();
            dto.setName(firstName != null && lastName != null ? firstName + " " + lastName : "Unknown");
        } else {
            dto.setName("Unknown");
        }

        dto.setPic(supplier.getUser().getProfileimage() != null ? supplier.getUser().getProfileimage().getUrl() : null);
        dto.setEmail(supplier.getUser().getEmail());

        return dto;
    }
}
