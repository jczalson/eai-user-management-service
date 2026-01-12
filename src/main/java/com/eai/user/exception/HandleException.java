package com.eai.user.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eai.user.entities.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController{

    private static final String EXPIRED = "expired";
    private static final String SESSION_IS_EXPIRED = "Your session is expired, please log in again";


    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object arg1, HttpHeaders arg2,
            HttpStatusCode statusCode, WebRequest arg4) {
                log.error(exception.getMessage());
       return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.resolve(statusCode.value()))
                .statusCode(statusCode.value())
                .reason(exception.getMessage())
                .developerMessage(exception.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),statusCode);
                
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors(); 
        String fieldMessage = fieldErrors.stream().map(field->field.getDefaultMessage()).collect(Collectors.joining(", "));    
        // String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));    
        log.error(ex.getMessage());
        return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.resolve(status.value()))
                .statusCode(status.value())
                .reason(fieldMessage)
                .developerMessage(ex.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),status);
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
       log.error(exception.getMessage());
        return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reason(exception.getMessage())
                .developerMessage(exception.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),HttpStatus.BAD_REQUEST);
    }

     @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException exception) {
       log.error(exception.getMessage());
        return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reason(exception.getMessage())
                .developerMessage(exception.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
       log.error(exception.getMessage());
        return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason(exception.getMessage().contains(EXPIRED)?SESSION_IS_EXPIRED:exception.getMessage())
                .developerMessage(exception.getMessage().contains(EXPIRED)?SESSION_IS_EXPIRED:exception.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
       log.error("Bad credentials error: "+exception.getMessage());
        return new ResponseEntity<>(
         HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reason("Incorrect email or password")
                .developerMessage(exception.getMessage())
                .timeStamp(LocalDateTime.now().toString())
                .build(),HttpStatus.BAD_REQUEST);
    }

}
