package com.ing.hubs.exception.enrollment;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class NotEnrolledInCourseException extends DevSchoolException {

    public NotEnrolledInCourseException() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Student is not enrolled in this course");

    }
}
