package com.camelsoft.rayaserver.Controller.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Request;
import com.camelsoft.rayaserver.Repository.Project.RequestRepository;
import com.camelsoft.rayaserver.Request.project.EventRequest;
import com.camelsoft.rayaserver.Request.project.RequestOfEvents;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.File.FilesStorageServiceImpl;
import com.camelsoft.rayaserver.Services.Project.EventService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/requests")
public class RequestController {

    @Autowired
    private EventService service;
    @Autowired
    private FilesStorageServiceImpl filesStorageService;



}
