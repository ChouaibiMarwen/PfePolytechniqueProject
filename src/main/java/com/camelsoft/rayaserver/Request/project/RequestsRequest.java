package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestCorrespondant;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Data
public class RequestsRequest {

    private String type ;
    private RequestState status;
    private String title;
    private String description;
   /* private RequestCorrespondant correspondant;*/
    private Set<Long> invoicesId = new HashSet<>();

    public RequestsRequest() {
    }
}
