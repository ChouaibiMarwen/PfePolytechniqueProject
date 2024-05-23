package com.camelsoft.rayaserver.Request.project;

import com.camelsoft.rayaserver.Enum.Project.Vehicles.*;
import lombok.Data;

import javax.persistence.Column;
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
    private String carmake;
    private String mileage;
    private String year;
    private String doors;
    private AvailiabilityEnum availiability;
    private ConditionEnum condition;
    private TransmissionTypeEnum transmissiontype;
}
