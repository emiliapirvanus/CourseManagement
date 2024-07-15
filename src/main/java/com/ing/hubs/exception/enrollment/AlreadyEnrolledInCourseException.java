package com.ing.hubs.exception.enrollment;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class AlreadyEnrolledInCourseException extends DevSchoolException {
    public AlreadyEnrolledInCourseException() {
        this.setHttpStatus(HttpStatus.CONFLICT);
        this.setMessage("User is already part of this course");
    }
}
