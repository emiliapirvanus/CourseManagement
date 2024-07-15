package com.ing.hubs.service;

import com.ing.hubs.dto.course.*;
import com.ing.hubs.dto.course.grade.GradeDto;
import com.ing.hubs.dto.course.grade.GradeResponseDto;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.StudentCourse;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.entity.teacher.Teacher;
import com.ing.hubs.exception.course.CourseDecreaseAttendeesException;
import com.ing.hubs.exception.course.CourseDuplicateException;
import com.ing.hubs.exception.course.CourseNotAssignedToTeacherException;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.enrollment.EnrollmentNotFound;
import com.ing.hubs.exception.enrollment.NoEnrolledCoursesException;
import com.ing.hubs.exception.enrollment.NotEnrolledInCourseException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrollmentRepository;
import com.ing.hubs.repository.StudentCourseRepository;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.security.JwtDecoder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CourseService {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
    private StudentCourseRepository studentCourseRepository;

    private StudentService studentService;
    private TeacherService teacherService;

    private ModelMapper modelMapper;
    private JwtDecoder jwtDecoder;

    public Course getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
    }

    public CourseResponseDto getCourseResponseById(UUID courseId) {
        return modelMapper.map(
                courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new),
                CourseResponseDto.class);
    }

    public List<StudentCourseResponseDto> getSessionCoursesAndGrades() {
        var student = getStudent(jwtDecoder.getSessionUserId());
        var courses = student.getStudentCourses();

        if(courses.isEmpty()) throw new NoEnrolledCoursesException();

        return courses.stream()
                .map(course -> modelMapper.map(course, StudentCourseResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<CourseResponseDto> getCourses() {
        var courses = courseRepository.findAll();

        return courses.stream()
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .collect(Collectors.toList());
    }

    public CourseResponseDto createCourse(CourseDto dto) {
        if (courseRepository.findByName(dto.getName()).isPresent()) {
            throw new CourseDuplicateException();
        }

        dto.setTeacherId(jwtDecoder.getSessionUserId());

        var teacher = getTeacher(dto.getTeacherId());
        var entity = courseRepository.save(modelMapper.map(dto, Course.class));
        teacher.addCourse(entity);

        return modelMapper.map(entity, CourseResponseDto.class);
    }

    public StudentCourseIdWrapper manageEnrollment(StudentCourseDto dto, EnrollmentStatus enrollmentStatus) {
        var course = getCourseById(dto.getCourseId());
        var student = getStudent(dto.getStudentId());

        validateEnrollment(course, student, dto, enrollmentStatus);

        if(enrollmentStatus.equals(EnrollmentStatus.ACCEPTED)) {
            studentCourseRepository.save(StudentCourse.builder()
                    .student(student)
                    .course(course)
                    .build());
            courseRepository.save(course);
        }

        return new StudentCourseIdWrapper(student.getId());
    }

    public GradeResponseDto gradeStudent(GradeDto dto) {
        var studentCourse = studentCourseRepository
                .findByCourseIdAndStudentId(dto.getCourseId(), dto.getStudentId())
                .orElseThrow(NotEnrolledInCourseException::new);

        studentCourse.setGrade(dto.getGrade());
        studentCourse.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(studentCourseRepository.save(studentCourse), GradeResponseDto.class);
    }

    @Transactional
    public CourseResponseDto updateCourse(CourseUpdateDto dto) {
        var teacher = getTeacher(jwtDecoder.getSessionUserId());
        var course = getCourseById(dto.getCourseId());

        if (!teacher.getCourses().contains(course)) {
            throw new CourseNotAssignedToTeacherException();
        }

        if (dto.getName() != null) {
            course.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }

        if (dto.getMaxAttendees() != null) {
            if (dto.getMaxAttendees() > course.getMaxAttendees()) {
                course.setMaxAttendees(dto.getMaxAttendees());
            } else {
                throw new CourseDecreaseAttendeesException();
            }
        }

        return modelMapper.map(courseRepository.save(course), CourseResponseDto.class);
    }

    private void validateEnrollment(Course course, Student student, StudentCourseDto dto, EnrollmentStatus enrollmentStatus) {
        var teacher = getTeacher(jwtDecoder.getSessionUserId());

        if (!teacher.getCourses().contains(course)) {
            throw new CourseNotAssignedToTeacherException();
        }

        var enrollment = student.findPendingEnrollment(dto.getCourseId(), jwtDecoder.getSessionUserId())
                .orElseThrow(EnrollmentNotFound::new);

        enrollment.setStatus(enrollmentStatus);
        enrollmentRepository.save(enrollment);
    }

    private Teacher getTeacher(UUID id) {
        return teacherService.getTeacherById(id);
    }

    private Student getStudent(UUID id) {
        return studentService.getStudentById(id);
    }
}
