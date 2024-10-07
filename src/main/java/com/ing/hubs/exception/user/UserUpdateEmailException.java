package com.ing.hubs.exception.user;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class UserUpdateEmailException extends DevSchoolException {
    public UserUpdateEmailException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Email can't be the same as the old one");
    }
}
