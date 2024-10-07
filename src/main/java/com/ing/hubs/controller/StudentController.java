package com.ing.hubs.controller;

import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.dto.student.StudentUpdateDto;
import com.ing.hubs.service.StudentService;
import com.ing.hubs.service.UserService;
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
@RequestMapping(path = "/students")
public class StudentController {
    private StudentService studentService;

    @GetMapping(path = "/{studentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public StudentResponseDto getStudentById(@Valid @PathVariable("studentId")UUID studentId) {
        return studentService.getStudentResponseById(studentId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<StudentResponseDto> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping (path = "/me")
    @ResponseStatus(value = HttpStatus.OK)
    public StudentResponseDto getStudentFromSession() {
        return studentService.getStudentFromSession();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public StudentResponseDto createStudent(@Valid @RequestBody StudentDto studentDto) {
       return studentService.addStudent(studentDto);
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.OK)
    public StudentResponseDto updateStudent(@Valid @RequestBody StudentUpdateDto studentUpdateDto) {
        return studentService.updateStudent(studentUpdateDto);
    }

//    @DeleteMapping
//    @ResponseStatus (value = HttpStatus.OK)
//    public StringResponseDto deleteStudent() {
//        return studentService.deleteStudent();
//    }

}
