package com.ing.hubs.service;

import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentIdWrapper;
import com.ing.hubs.dto.course.enrollment.EnrollmentResponseDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.StudentCourse;
import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.enrollment.AlreadyEnrolledInCourseException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrollmentRepository;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.security.JwtDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentService enrollmentService;
    @Mock
    private ModelMapper modelMapper;

    private CourseResponseDto courseResponseDto;
    private Enrollment enrollment;
    private Student student;
    private Course course;
    private EnrollmentResponseDto enrollmentResponseDto;
    private StudentResponseDto studentResponseDto;

    @BeforeEach
    void setUp(){

        studentResponseDto = TestDataUtil.createStudentResponseDto();
        student = new Student();
        student.setId(UUID.randomUUID());
        course = new Course();
        course.setId(UUID.randomUUID());
        enrollment = TestDataUtil.createEnrollment(student, course);
        studentResponseDto = TestDataUtil.createStudentResponseDto();

        courseResponseDto = TestDataUtil.createCourseResponseDto();
        enrollmentResponseDto = TestDataUtil.createEnrollmentResponseDto(enrollment, studentResponseDto, courseResponseDto);
    }


    @Test
    void shouldReturnEnrollmentWhenGetEnrollment() {

        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        when(modelMapper.map(enrollment, EnrollmentResponseDto.class)).thenReturn(enrollmentResponseDto);

        List<EnrollmentResponseDto> result = enrollmentService.getEnrollments();

        assertEquals(enrollmentResponseDto, result.get(0));

    }

    @Test
    void shouldReturnSessionEnrollments() {

        UUID studentId = UUID.randomUUID();
        List<Enrollment> mockEnrollments = new ArrayList<>();
        mockEnrollments.add(new Enrollment());
        mockEnrollments.add(new Enrollment());

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);
        when(enrollmentRepository.findAllByStudentId(studentId)).thenReturn(Optional.of(mockEnrollments));

        List<EnrollmentResponseDto> result = enrollmentService.getSessionEnrollments();

        assertEquals(mockEnrollments.size(), result.size());
        verify(jwtDecoder, times(1)).getSessionUserId();
        verify(enrollmentRepository, times(1)).findAllByStudentId(studentId);
    }

    @Test
    void shouldSaveEnrollmentWhenSaveMethodCalled() {

        EnrollmentDto enrollmentDto = new EnrollmentDto();
        enrollmentDto.setCourseId(UUID.randomUUID());

        UUID studentId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);

        UUID courseId = enrollmentDto.getCourseId();
        Course course = new Course();
        course.setId(courseId);
        course.setMaxAttendees(30);

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);
        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(courseService.getCourseById(courseId)).thenReturn(course);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(new Enrollment());

        EnrollmentIdWrapper result = enrollmentService.save(enrollmentDto);

        assertNotNull(result);
        verify(jwtDecoder, times(1)).getSessionUserId();
        verify(studentService, times(1)).getStudentById(studentId);
        verify(courseService, times(1)).getCourseById(courseId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void shouldDeleteEnrollment() {

        UUID enrollmentId = UUID.randomUUID();
        EnrollmentIdWrapper enrollmentIdWrapper = new EnrollmentIdWrapper(enrollmentId);
        Enrollment mockEnrollment = new Enrollment();
        mockEnrollment.setStatus(EnrollmentStatus.PENDING);

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(mockEnrollment));
        doNothing().when(enrollmentRepository).delete(mockEnrollment);

        StringResponseDto result = enrollmentService.deleteEnrollment(enrollmentIdWrapper);

        assertEquals("Enrollment deleted: " + enrollmentId, result.getMessage());
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, times(1)).delete(mockEnrollment);
    }

}