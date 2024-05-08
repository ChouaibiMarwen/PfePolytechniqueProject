package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.File_model;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class EventRequest {
    private String title;
    private String description;
    private Date eventDate;
    private File_model attachment;
    private Set<Role> assignedto = new HashSet<>();
}
