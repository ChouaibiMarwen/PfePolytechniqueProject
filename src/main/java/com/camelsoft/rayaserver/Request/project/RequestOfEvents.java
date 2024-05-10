package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
public class RequestOfEvents {
    private String title;
    private String description;
    private Date eventDate;
    private EventStatus status;
    private Set<RoleEnum> assignedto = new HashSet<>();

    public RequestOfEvents() {
    }
}
