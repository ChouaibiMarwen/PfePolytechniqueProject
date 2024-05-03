package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Models.country.Root;
import lombok.Data;

@Data
public class RootDto {

    private Long id;
    private String name;
    private String phone_code;
    private String currency;
    private String currency_symbol;
    private String latitude;
    private String longitude;
    private String emoji;


    public RootDto convertRootToDTO(Root root) {
        RootDto dto = new RootDto();
        dto.setId(root.getId());
        dto.setName(root.getName());
        dto.setPhone_code(root.getPhone_code());
        dto.setCurrency(root.getCurrency());
        dto.setCurrency_symbol(root.getCurrency_symbol());
        dto.setLatitude(root.getLatitude());
        dto.setLongitude(root.getLongitude());
        dto.setEmoji(root.getEmoji());
        return dto;
    }
}
