package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class ScheduleStartDateAfterEndDateException extends DevSchoolException {
    public ScheduleStartDateAfterEndDateException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Start date of the course can't be after the end date");
    }
}
