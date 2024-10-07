package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class CourseDecreaseAttendeesException extends DevSchoolException {
    public CourseDecreaseAttendeesException() {
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("Cannot decrease the number of attendees for the course");
    }
}
