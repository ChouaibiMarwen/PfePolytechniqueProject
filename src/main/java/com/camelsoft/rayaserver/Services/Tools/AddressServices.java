package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Repository.Tools.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServices {

    @Autowired
    private AddressRepository addressRepository;


    public Address save(Address address) {
        return addressRepository.save(address);
    }
}
