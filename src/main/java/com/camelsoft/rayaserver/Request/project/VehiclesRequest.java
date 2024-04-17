package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Vehicles.BodyStyle;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.FuelType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class VehiclesRequest {
    private String carmodel;
    private String color;
    private String carvin;
    private Double enginesize=0D;
    private FuelType fueltype = FuelType.NONE;
    private BodyStyle bodystyle = BodyStyle.NONE;
    private Set<String> exteriorfeatures = new HashSet<>();
    private Set<String> interiorfeatures = new HashSet<>();
    private String description;
    private Integer stock = 0;
}
