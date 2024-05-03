package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.State;
import com.camelsoft.rayaserver.Repository.Tools.AddressRepository;
import com.camelsoft.rayaserver.Request.Tools.AddressRequest;
import com.camelsoft.rayaserver.Services.Country.CountriesServices;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AddressServices {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CountriesServices countriesServices;


    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address findById(Long addressId) {
        try {
            if(!this.addressRepository.existsById(addressId))
                throw new NotFoundException(String.format("Address with id " + addressId + " is not found " ));
            return addressRepository.findById(addressId).get();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public Address findByIdWithoutEXCEPTION(Long addressId) {
        return addressRepository.findById(addressId).get();
    }


    public Address updateAddress(Address address, AddressRequest request) {
        if (request.getAddressline1() != null && !request.getAddressline1().isEmpty()) {
            address.setAddressline1(request.getAddressline1());
        }
        if (request.getAddressline2() != null) {
            address.setAddressline2(request.getAddressline2());
        }
        if (request.getPostcode() != null && !request.getPostcode().isEmpty()) {
            address.setPostcode(request.getPostcode());
        }
        if (request.getBuilding() != null) {
            address.setBuilding(request.getBuilding());
        }
        if (request.getUnitnumber() != null) {
            address.setUnitnumber(request.getUnitnumber());
        }
        if (request.getStreetname() != null) {
            address.setStreetname(request.getStreetname());
        }
        if (request.getPrimaryaddress() != null) {
            address.setPrimaryaddress(request.getPrimaryaddress());
        }
        if(request.getCountryName() != null){
            Root country = this.countriesServices.countrybyname(request.getCountryName());
            if(country != null){
                address.setCountry(country);
            }
        }

        if(request.getCityName() != null){
            State city = this.countriesServices.Statebyname(request.getCityName());
            if(city != null){
                address.setCity(city);
            }
        }

        return addressRepository.save(address);
    }
}
