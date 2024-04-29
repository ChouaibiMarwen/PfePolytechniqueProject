package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Repository.Tools.BillingAddressRepository;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BillingAddressService {

    @Autowired
    private BillingAddressRepository repository;

    public BillingAddress saveBiLLAddress(BillingAddress billingAddress) {
        try {
            return this.repository.save(billingAddress);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
}
