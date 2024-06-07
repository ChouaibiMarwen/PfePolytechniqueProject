package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequestsRequest {

    private String type ;
    private RequestState status =  RequestState.NONE;
    private String title;
    private String description;
   /* private RequestCorrespondant correspondant;*/
    private Set<Long> invoicesId = new HashSet<>();

    public RequestsRequest() {
    }
}
