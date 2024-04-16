package com.camelsoft.rayaserver.Tools.Exception;


import com.camelsoft.rayaserver.Tools.Error.ApiBaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApiBaseException {

    public  ConflictException(String message){
        super(message);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
