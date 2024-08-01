package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Event.EventStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
public class RequestOfEvents {
    private String title;
    private String description;
    private Date eventDate;
    private Date enddate;
    private EventStatus status;
    private Set<RoleEnum> assignedto = new HashSet<>();
    private Set<Long> categoriesId = new HashSet<>();

    public RequestOfEvents() {
    }
}
