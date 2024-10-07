package com.ing.hubs.service;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.teacher.TeacherDto;
import com.ing.hubs.dto.teacher.TeacherResponseDto;
import com.ing.hubs.entity.teacher.Teacher;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.exception.user.UserRegisteredException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.TeacherRepository;
import com.ing.hubs.security.JwtDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TeacherService {
    private TeacherRepository teacherRepository;
    private CourseRepository courseRepository;
    private ModelMapper modelMapper;
    private UserService userService;
    private JwtDecoder jwtDecoder;

    public TeacherResponseDto getTeacherResponseById(UUID teacherId) {
        return modelMapper
                .map(teacherRepository
                                .findById(teacherId)
                                .orElseThrow(UserNotFoundException::new),
                TeacherResponseDto.class);
    }

    public Teacher getTeacherById(UUID teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
    }
    public TeacherResponseDto createTeacher(TeacherDto teacherDto) {
        try {
            var savedTeacherEntity = teacherRepository.save(
                    modelMapper.map(
                            teacherDto,
                            Teacher.class
                    )
            );
            userService.saveTeacher(teacherDto);

            return modelMapper.map(
                    savedTeacherEntity,
                    TeacherResponseDto.class);
        } catch (DataIntegrityViolationException ex) {
            throw new UserRegisteredException();
        }
    }

    public List<CourseResponseDto> getCourseByTeacherId() {
        var teacherId = jwtDecoder.getSessionUserId();
        teacherRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        var courses = courseRepository.findAllByTeacherId(teacherId);

        return courses.stream()
                .map(course -> modelMapper.map(course,CourseResponseDto.class))
                .toList();
    }
}
