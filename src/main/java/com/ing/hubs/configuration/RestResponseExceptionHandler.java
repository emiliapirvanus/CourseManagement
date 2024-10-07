package com.ing.hubs.configuration;

import com.ing.hubs.exception.DevSchoolException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {DevSchoolException.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        var customException = (DevSchoolException) ex;
        var bodyOfResponse = new ExceptionBody(customException.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), customException.getHttpStatus(), request);
    }

    @Data
    @AllArgsConstructor
    static
    class ExceptionBody {
        private String message;
    }
}