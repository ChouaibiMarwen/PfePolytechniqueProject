package com.camelsoft.rayaserver.Request.project;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class VehiclesMediaRequest {
    private MultipartFile frontviewimage;

    private MultipartFile rearviewimage;

    private MultipartFile interiorviewimage;

    private MultipartFile sideviewimageleft;

    private MultipartFile sideviewimageright;

    private List<MultipartFile> additionalviewimages = new ArrayList<>();



}
