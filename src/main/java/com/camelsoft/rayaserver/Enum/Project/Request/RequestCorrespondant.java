package com.camelsoft.rayaserver.Enum.Project.Request;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Request;
import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
 @Data
public class RequestCorrespondant {

    private String title;
    private String description;
    private users creator;
    private MultipartFile attachment;

    public RequestCorrespondant() {
    }
}
