package com.ing.hubs.controller;

import com.ing.hubs.dto.course.*;
import com.ing.hubs.dto.course.grade.GradeDto;
import com.ing.hubs.dto.course.grade.GradeResponseDto;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import com.ing.hubs.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/courses")
public class CourseController {
    private CourseService courseService;

    @GetMapping(path = "/{courseId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CourseResponseDto getCourseById (@Valid @PathVariable("courseId")UUID courseId) {
        return courseService.getCourseResponseById(courseId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CourseResponseDto> getCourses() {
        return courseService.getCourses();
    }

    @GetMapping (path = "/me")
    @ResponseStatus(value = HttpStatus.OK)
    public List<StudentCourseResponseDto> getCoursesAndGrades() {
        return courseService.getSessionCoursesAndGrades();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CourseResponseDto createCourse(@Valid @RequestBody CourseDto dto) {
        return courseService.createCourse(dto);
    }

    @PostMapping (path = "/approve")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public StudentCourseIdWrapper approveStudent(@Valid @RequestBody StudentCourseDto dto) {
        return courseService.manageEnrollment(dto, EnrollmentStatus.ACCEPTED);
    }

    @PostMapping (path = "/deny")
    @ResponseStatus(value = HttpStatus.OK)
    public StudentCourseIdWrapper denyStudent(@Valid @RequestBody StudentCourseDto dto) {
        return courseService.manageEnrollment(dto, EnrollmentStatus.DENIED);
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.OK)
    public CourseResponseDto updateCourse(@Valid @RequestBody CourseUpdateDto dto) {
        return courseService.updateCourse(dto);
    }

    @PatchMapping(path = "/grade")
    @ResponseStatus(value = HttpStatus.OK)
    public GradeResponseDto gradeStudent(@RequestBody @Valid GradeDto dto) {
        return courseService.gradeStudent(dto);
    }
}
