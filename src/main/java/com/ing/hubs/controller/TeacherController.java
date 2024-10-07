package com.ing.hubs.controller;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.teacher.TeacherDto;
import com.ing.hubs.dto.teacher.TeacherResponseDto;
import com.ing.hubs.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping(path = "/{teacherId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TeacherResponseDto getTeacherResponseById(@Valid @PathVariable("teacherId") UUID teacherId) {
        return teacherService.getTeacherResponseById(teacherId);
    }

    @GetMapping(path = "/me/courses")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CourseResponseDto> getTeacherCourses() {
        return teacherService.getCourseByTeacherId();
    }
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TeacherResponseDto createTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        return teacherService.createTeacher(teacherDto);
    }

}
