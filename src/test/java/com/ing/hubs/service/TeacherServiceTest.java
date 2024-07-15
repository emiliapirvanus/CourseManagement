package com.ing.hubs.service;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.teacher.TeacherDto;
import com.ing.hubs.dto.teacher.TeacherResponseDto;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.teacher.Teacher;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.exception.user.UserRegisteredException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.TeacherRepository;
import com.ing.hubs.security.JwtDecoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    @Mock
    private JwtDecoder jwtDecoder;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void shouldReturnTeacherWhenTeacherIdExists() {

        UUID randomId = UUID.randomUUID();
        Teacher teacher = new Teacher();
        teacher.setId(randomId);

        when(teacherRepository.findById(randomId)).thenReturn(Optional.of(teacher));

        Teacher teacherById = teacherService.getTeacherById(randomId);

        assertEquals(teacher, teacherById);

    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenTeacherIdDoesNotExist() {

        UUID teacherId = UUID.randomUUID();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> teacherService.getTeacherById(teacherId));
    }

    @Test
    void shouldSaveTeacherAndReturnResponseDto() {

        TeacherDto teacherDto = new TeacherDto();
        Teacher mockTeacher = new Teacher();
        TeacherResponseDto mockResponseDto = new TeacherResponseDto();

        when(modelMapper.map(teacherDto, Teacher.class)).thenReturn(mockTeacher);
        when(teacherRepository.save(mockTeacher)).thenReturn(mockTeacher);
        when(modelMapper.map(mockTeacher, TeacherResponseDto.class)).thenReturn(mockResponseDto);

        TeacherResponseDto result = teacherService.createTeacher(teacherDto);

        assertNotNull(result);
        assertSame(mockResponseDto, result);
        verify(teacherRepository, times(1)).save(mockTeacher);
        verify(userService, times(1)).saveTeacher(teacherDto);
    }

    @Test
    void shouldThrowUserRegisteredExceptionWhenDataIntegrityViolationExceptionOccurs() {

        TeacherDto teacherDto = new TeacherDto();

        when(modelMapper.map(teacherDto, Teacher.class)).thenReturn(new Teacher());
        when(teacherRepository.save(any(Teacher.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UserRegisteredException.class, () -> teacherService.createTeacher(teacherDto));

        verify(userService, never()).saveTeacher(teacherDto);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenTeacherDoesNotExist() {

        UUID teacherId = UUID.randomUUID();

        when(jwtDecoder.getSessionUserId()).thenReturn(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> teacherService.getCourseByTeacherId());

    }

    @Test
    void shouldGetCourseByTeacherId() {
        UUID teacherId = UUID.randomUUID();

        when(jwtDecoder.getSessionUserId()).thenReturn(teacherId);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(new Teacher()));
        when(courseRepository.findAllByTeacherId(teacherId)).thenReturn(getMockCourses());
        when(modelMapper.map(any(), eq(CourseResponseDto.class))).thenReturn(getMockCourseResponseDto());

        List<CourseResponseDto> result = teacherService.getCourseByTeacherId();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(jwtDecoder, times(1)).getSessionUserId();
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(courseRepository, times(1)).findAllByTeacherId(teacherId);
        verify(modelMapper, times(2)).map(any(), eq(CourseResponseDto.class)); // Assuming two courses are mapped
    }

    private List<Course> getMockCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        courses.add(new Course());
        return courses;
    }


    private CourseResponseDto getMockCourseResponseDto() {
        return new CourseResponseDto();
    }


}