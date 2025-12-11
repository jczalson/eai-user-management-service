package com.eai.user.entities;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {

    private String message;

    private String reason;

    private String developerMessage;

    private String timeStamp;

    private HttpStatus status;

    private int statusCode;

    private Map<?,?> data;
}
