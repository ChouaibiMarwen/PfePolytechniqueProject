package com.smarty.pfeserver.Tools.Exception;




import com.smarty.pfeserver.Tools.Error.ApiBaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiBaseException {
    public NotFoundException(String message){
        super(message);
    }
    @Override
    public HttpStatus getStatusCode(){
        return HttpStatus.NOT_FOUND;
    }
}
