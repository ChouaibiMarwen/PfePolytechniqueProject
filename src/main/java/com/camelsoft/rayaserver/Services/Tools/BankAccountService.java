package com.camelsoft.rayaserver.Services.Tools;

import com.camelsoft.rayaserver.Models.Tools.BankInformation;
import com.camelsoft.rayaserver.Models.Tools.BillingAddress;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Tools.BankAccountRepository;
import com.camelsoft.rayaserver.Request.Tools.BankInformationRequest;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;


    public BankInformation saveBankInformation(BankInformation bankInformation) {
        try {
            return this.bankAccountRepository.save(bankInformation);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public BankInformation findById(Long bankInfoId) {
        try {
            if(!this.bankAccountRepository.existsById(bankInfoId))
                throw new NotFoundException(String.format("Bank information with id " + bankInfoId + " is not found " ));
            return bankAccountRepository.findById(bankInfoId).get();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }


    public BankInformation findByIdWithoutException(Long bankInfoId) {

            return bankAccountRepository.findById(bankInfoId).get();

    }

    public BankInformation updateBankInfo(BankInformation bankInformation ,BankInformationRequest request){
        if (request.getBank_name() != null) {
            bankInformation.setBankname(request.getBank_name());
        }
        if (request.getAccountHolderName() != null) {
            bankInformation.setAccountname(request.getAccountHolderName());
        }
        if (request.getAcountNumber() != null) {
            bankInformation.setRip(request.getAcountNumber());
        }
        return  saveBankInformation(bankInformation);
    }


    public void deleteBankInformation(BankInformation bankInformation){
        try {
            users user = bankInformation.getUser();
            user.getBankinformations().remove(bankInformation);
            bankInformation.setUser(null);
            bankAccountRepository.delete(bankInformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
