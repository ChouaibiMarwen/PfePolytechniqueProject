package com.camelsoft.rayaserver.Request.project;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashSet;
import java.util.Set;

@Data
public class VehiclesMediaRequest {
    private MultipartFile frontviewimage;

    private MultipartFile rearviewimage;

    private MultipartFile interiorviewimage;

    private MultipartFile sideviewimageleft;

    private MultipartFile sideviewimageright;

    private Set<MultipartFile> additionalviewimages = new HashSet<>();



}
