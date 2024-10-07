package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class ScheduleDateInPastException extends DevSchoolException {
    public ScheduleDateInPastException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Date can't be set in the past");
    }
}
