package com.camelsoft.rayaserver.Response.Tools;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String presignedUrl;
    private String uri;
    private String error_message;

    public FileUploadResponse() {
    }
}

