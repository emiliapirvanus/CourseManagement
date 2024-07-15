package com.ing.hubs.exception.user;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class UserRegisteredException extends DevSchoolException {
    public UserRegisteredException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("User cannot be registered");
    }
}
