package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class CourseLimitReachedException extends DevSchoolException {
    public CourseLimitReachedException() {
        this.setHttpStatus(HttpStatus.BAD_GATEWAY);
        this.setMessage("Course limit reached");
    }
}
