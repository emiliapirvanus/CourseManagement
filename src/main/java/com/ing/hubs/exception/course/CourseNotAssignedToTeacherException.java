package com.ing.hubs.exception.course;

import com.ing.hubs.exception.DevSchoolException;
import org.springframework.http.HttpStatus;

public class CourseNotAssignedToTeacherException extends DevSchoolException {
    public CourseNotAssignedToTeacherException() {
        this.setHttpStatus(HttpStatus.NO_CONTENT);
        this.setMessage("The course does not belong to the teacher");
    }
}
