package com.ing.hubs.exception.enrollment;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class NoEnrolledCoursesException extends DevSchoolException {
    public NoEnrolledCoursesException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Student is not enrolled in any courses");
    }
}
