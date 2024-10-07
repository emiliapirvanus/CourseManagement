package com.ing.hubs.exception.user;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class PasswordIncorrectException extends DevSchoolException {
    public PasswordIncorrectException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Passwords don't match");
    }
}
