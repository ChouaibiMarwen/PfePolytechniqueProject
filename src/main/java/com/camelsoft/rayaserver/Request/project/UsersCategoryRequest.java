package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UsersCategoryRequest {
    private String name;
    private String description;
    private RoleEnum categoryAssignedRole;
    private Set<Long>  usersIds = new HashSet<>();
}
