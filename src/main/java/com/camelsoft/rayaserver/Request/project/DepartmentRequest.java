package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DepartmentRequest {
    private String name;
    private Set<RoleDepartment> roles= new HashSet<>();

}
