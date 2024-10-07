package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class CourseDuplicateException extends DevSchoolException {
    public CourseDuplicateException() {
        this.setHttpStatus(HttpStatus.CONFLICT);
        this.setMessage("A course is already created with that name");
    }
}
