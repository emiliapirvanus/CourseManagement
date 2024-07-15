package com.ing.hubs.controller;


import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentIdWrapper;
import com.ing.hubs.dto.course.enrollment.EnrollmentResponseDto;
import com.ing.hubs.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/enrollments")
public class EnrollmentController {
    private EnrollmentService enrollmentService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EnrollmentResponseDto> getEnrollments() {
        return enrollmentService.getEnrollments();
    }

    @GetMapping("/pending")
    @ResponseStatus(value = HttpStatus.OK)
    public List<EnrollmentResponseDto> getPendingEnrollmentsForTeacher() {
        return enrollmentService.getPendingEnrollments();
    }

    @GetMapping (path = "/me")
    @ResponseStatus(value = HttpStatus.OK)
    public List<EnrollmentResponseDto> getSessionEnrollments() {
        return enrollmentService.getSessionEnrollments();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EnrollmentIdWrapper createEnrollment(@RequestBody @Valid EnrollmentDto enrollmentDto) {
        return enrollmentService.save(enrollmentDto);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    public StringResponseDto deleteEnrollment(@RequestBody EnrollmentIdWrapper enrollmentIdWrapper) {
        return enrollmentService.deleteEnrollment(enrollmentIdWrapper);
    }
}
