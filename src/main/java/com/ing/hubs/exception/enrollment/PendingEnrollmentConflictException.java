package com.ing.hubs.exception.enrollment;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class PendingEnrollmentConflictException extends DevSchoolException {
    public PendingEnrollmentConflictException() {
        this.setHttpStatus(HttpStatus.CONFLICT);
        this.setMessage("User has a pending enrollment for this course");
    }
}
