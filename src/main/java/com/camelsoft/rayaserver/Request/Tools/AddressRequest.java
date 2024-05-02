package com.camelsoft.rayaserver.Request.Tools;

import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.State;
import lombok.Data;

import javax.persistence.Entity;

@Data
public class AddressRequest {
    private String addressline1;
    private String addressline2;
    private String postcode;
    private String building;
    private String unitnumber;
    private String streetname;
    private Boolean primaryaddress;
    private String countryName;
    private String cityName;

    public AddressRequest() {}



}
