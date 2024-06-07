package com.camelsoft.rayaserver.Request.project;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdsRequest {

    private String url;
    private String description;
    private List<MultipartFile> attachments = new ArrayList<>();
}
