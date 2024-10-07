package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class ScheduleConflictException extends DevSchoolException {
    public ScheduleConflictException() {
        this.setHttpStatus(HttpStatus.CONFLICT);
        this.setMessage("Schedule conflict with existing ones");
    }
}
