package com.ing.hubs.exception.user;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DevSchoolException {
    public UserNotFoundException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("User with those credentials not found");
    }
}
