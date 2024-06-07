package com.camelsoft.rayaserver.Enum.Project.Request;

import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestCorrespondant {

    private String title;
    private String description;
    private users creator;
    private MultipartFile attachment;

    public RequestCorrespondant() {
    }
}
