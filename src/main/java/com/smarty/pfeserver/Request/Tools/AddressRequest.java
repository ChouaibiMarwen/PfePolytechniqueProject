package com.smarty.pfeserver.Request.Tools;

import lombok.Data;

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
