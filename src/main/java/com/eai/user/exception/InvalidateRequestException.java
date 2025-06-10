package com.eai.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidateRequestException extends RuntimeException{

    public InvalidateRequestException(String message){
        super(message);
    }
    
    public  InvalidateRequestException(String message, Throwable cause){
        super(message,cause);
    }
}
