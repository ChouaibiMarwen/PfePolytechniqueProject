package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Models.File.File_model;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class AdsRequest {

    private String url;
    private String description;
    private List<MultipartFile> attachments = new ArrayList<>();
}
