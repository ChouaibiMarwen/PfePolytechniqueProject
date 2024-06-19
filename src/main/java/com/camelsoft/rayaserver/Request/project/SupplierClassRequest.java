package com.camelsoft.rayaserver.Request.project;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SupplierClassRequest {
    private String name;
    private String description;
    private Set<Long> usersId = new HashSet<>();

}
