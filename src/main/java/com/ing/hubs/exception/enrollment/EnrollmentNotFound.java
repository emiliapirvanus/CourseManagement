package com.ing.hubs.exception.enrollment;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class EnrollmentNotFound extends DevSchoolException {
    public EnrollmentNotFound() {
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("Enrollment(s) not found for this student or course");
    }
}
