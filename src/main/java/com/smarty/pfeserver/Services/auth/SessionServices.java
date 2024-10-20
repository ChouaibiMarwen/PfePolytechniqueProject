package com.smarty.pfeserver.Services.auth;




import com.smarty.pfeserver.Models.Auth.SessionModel;
import com.smarty.pfeserver.Repository.Auth.SessionRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SessionServices {
    @Autowired
    private SessionRepository repository;
    public List<SessionModel> findbyusername(String username){
        try {
            return this.repository.findAllByUsername(username);

        }catch (NoSuchElementException ex){
        throw  new NotFoundException(String.format("error"));
        }
    }
    public List<SessionModel> findbyemail(String email){
        try {
            return this.repository.findAllByEmail(email);

        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("error"));
        }
    }
    public List<SessionModel> findbyuserid(int userid){
        try {
            return this.repository.findAllByUserid(userid);

        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("error"));
        }
    }
    public SessionModel save_session(SessionModel session){
        try {
            return this.repository.save(session);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("error"));
        }
    }
    public void deletesessionbyid(int sessionid){
        try {
            this.repository.deleteById(sessionid);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("error"));
        }
    }
    public void deleteAllByUserid(int userid){
        try {
            this.repository.deleteAllByUserid(userid);
        }catch (NoSuchElementException ex){
            throw  new NotFoundException(String.format("error"));
        }
    }
}
