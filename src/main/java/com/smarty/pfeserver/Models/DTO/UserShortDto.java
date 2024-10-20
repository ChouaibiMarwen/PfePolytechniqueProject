package com.smarty.pfeserver.Models.DTO;

import com.smarty.pfeserver.Models.Tools.PersonalInformation;
import com.smarty.pfeserver.Models.User.users;
import lombok.Data;

@Data
public class UserShortDto {
    private Long id;
    private String name;
    private String pic;
    private String email;


    public static UserShortDto mapToUserShortDTO(users user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());

        PersonalInformation personalInformation = user.getPersonalinformation();
        if (personalInformation != null) {
            String firstName = personalInformation.getFirstnameen();
            String lastName = personalInformation.getLastnameen();
            dto.setName(firstName != null && lastName != null ? firstName + " " + lastName : "Unknown");
        } else {
            dto.setName("Unknown");
        }

        dto.setPic(user.getProfileimage() != null ? user.getProfileimage().getUrl() : null);
        dto.setEmail(user.getEmail());


        return dto;
    }
}
