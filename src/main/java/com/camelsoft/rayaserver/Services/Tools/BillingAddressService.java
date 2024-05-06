package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.Address;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Tools.BillingAddressRepository;
import com.camelsoft.rayaserver.Request.Tools.BillingAddressRequest;
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



    public BillingAddress findBillingAddressById(Long id) {
        try {
            if(!this.repository.existsById(id))
                throw new NotFoundException(String.format("user with id " + id + " is not found " ));
            return repository.findById(id).get();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }



    public BillingAddress updateBillingAddress( BillingAddress billingAddress,  BillingAddressRequest request){
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            billingAddress.setEmail(request.getEmail());
        }
        if (request.getFirstname() != null && !request.getFirstname().isEmpty()) {
            billingAddress.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null && !request.getLastname().isEmpty()) {
            billingAddress.setLastname(request.getLastname());
        }
        if (request.getBillingaddress() != null && !request.getBillingaddress().isEmpty()) {
            billingAddress.setBillingaddress(request.getBillingaddress());
        }
        if (request.getCountry() != null && !request.getCountry().isEmpty()) {
            billingAddress.setCountry(request.getCountry());
        }
        if (request.getZipcode() != null && !request.getZipcode().isEmpty()) {
            billingAddress.setZipcode(request.getZipcode());
        }
        if (request.getCity() != null && !request.getCity().isEmpty()) {
            billingAddress.setCity(request.getCity());
        }
        if (request.getState() != null && !request.getState().isEmpty()) {
            billingAddress.setState(request.getState());
        }
        if (request.getPhonenumber() != null && !request.getPhonenumber().isEmpty()) {
            billingAddress.setPhonenumber(request.getPhonenumber());
        }
        return repository.save(billingAddress);
    }

    public void deleteBillingAddress(BillingAddress address){
        try {
            users user = address.getUser();
            user.setBillingAddress(null);
            address.setUser(null);
            repository.delete(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
