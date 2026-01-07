package com.eai.user.exception;

import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;

import com.eai.user.entities.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws Exception {
        if (exception instanceof AccessDeniedException
                || exception instanceof BadCredentialsException) {
            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.BAD_REQUEST);
            writeResponse(httpResponse, response);
        } else if (exception instanceof JwtException) {
            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.UNAUTHORIZED);
            writeResponse(httpResponse, response);
        } else {
            HttpResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again",
                    HttpStatus.INTERNAL_SERVER_ERROR);
            writeResponse(httpResponse, response);
        }
        log.error(exception.getMessage());
    }

    private static void writeResponse(HttpResponse httpResponse, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out;
        try {
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus status)
            throws Exception {

        HttpResponse httpResponse = HttpResponse.builder()
                .status(status)
                .statusCode(status.value())
                .reason(message)
                .message("An error occurred while attempting to access resource")
                .timeStamp(LocalDateTime.now().toString())
                .build();
        return httpResponse;
    }
}
