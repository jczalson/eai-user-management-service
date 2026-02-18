package com.eai.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestApiException extends RuntimeException{

    public RestApiException(String message){
        super(message);
    }
    
    public  RestApiException(String message, Throwable cause){
        super(message,cause);
    }
}
