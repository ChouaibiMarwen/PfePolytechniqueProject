package com.smarty.pfeserver.Services.auth;


import com.smarty.pfeserver.Models.Auth.PasswordResetToken;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Auth.PasswordResetTokenRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class PasswordResetTokenServices {
    @Autowired
    private PasswordResetTokenRepository repository;
    public PasswordResetToken createPasswordResetTokenForUser(users user, String token) {
        try {
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            return this.repository.save(myToken);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("Could not save data"));
        }

    }
    public PasswordResetToken update_token_code(PasswordResetToken model) {
        try {
            return this.repository.save(model);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("Could not save data"));
        }
    }
    public void remove_code(users user) {
        try{
            List<PasswordResetToken> passwordResetTokenList=this.findAllbyuser(user);
            this.repository.deleteAll(passwordResetTokenList);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("Could not delete data"));
        }
    }
    public PasswordResetToken findbyuser(users user){
        try{
            return this.repository.findByUser(user);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("Could not find this user"));
        }
    }

    public List<PasswordResetToken> findAllbyuser(users user){
        try{
            return this.repository.findAllByUser(user);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("Could not find this user"));
        }
    }
    public static String generateRandomResetCode(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
    public String validatePasswordResetToken(users user,String token) {
        final PasswordResetToken passToken = this.repository.findByUser(user);
        if(!passToken.getToken().equals(token)){
            return "codeincorrect";
        }
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }
}
