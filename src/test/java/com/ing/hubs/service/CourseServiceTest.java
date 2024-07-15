//package com.ing.hubs.service;
//
//import com.ing.hubs.dto.course.CourseDto;
//import com.ing.hubs.dto.course.CourseResponseDto;
//import com.ing.hubs.dto.course.StudentCourseDto;
//import com.ing.hubs.dto.course.StudentCourseResponseDto;
//import com.ing.hubs.dto.student.StudentDto;
//import com.ing.hubs.entity.course.Course;
//import com.ing.hubs.entity.course.StudentCourse;
//import com.ing.hubs.entity.student.Student;
//import com.ing.hubs.entity.teacher.Teacher;
//import com.ing.hubs.repository.CourseRepository;
//import com.ing.hubs.repository.EnrollmentRepository;
//import com.ing.hubs.repository.StudentCourseRepository;
//import com.ing.hubs.repository.TeacherRepository;
//import com.ing.hubs.security.JwtDecoder;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class CourseServiceTest {
//    private CourseRepository courseRepository;
//    private EnrollmentRepository enrollmentRepository;
//    private StudentCourseRepository studentCourseRepository;
//    private TeacherRepository teacherRepository;
//    private StudentService studentService;
//    private TeacherService teacherService;
//    private ModelMapper modelMapper;
//    private JwtDecoder jwtDecoder;
//    private CourseService courseService;
//    private Course courseTest;
//    private Teacher teacher;
//    private Student student;
//
//    @BeforeEach
//    void setUp() {
//        courseRepository = Mockito.mock(CourseRepository.class);
//        enrollmentRepository = Mockito.mock(EnrollmentRepository.class);
//        studentCourseRepository = Mockito.mock(StudentCourseRepository.class);
//        studentService = Mockito.mock(StudentService.class);
//        teacherService = Mockito.mock(TeacherService.class);
//        teacherRepository = Mockito.mock(TeacherRepository.class);
//        modelMapper = Mockito.mock(ModelMapper.class);
//        jwtDecoder = Mockito.mock(JwtDecoder.class);
////        courseService = new CourseService(
////                courseRepository,
////                enrollmentRepository,
////                studentCourseRepository,
////                studentService,
////                teacherService,
////                modelMapper,
////                jwtDecoder);
//
////        teacher = Teacher.builder()
////                .id(UUID.randomUUID())
////                .firstName("John")
////                .lastName("Doe")
////                .email("john@example.com")
////                .build();
//
//        courseTest = Course.builder()
//                .id(UUID.randomUUID())
//                .name("OOP")
//                .description("OOP test course")
//                .maxAttendees(30)
//                .teacher(teacher)
//                .build();
//
////        student = Student.builder()
////                .id(UUID.randomUUID())
////                .firstName("Alice")
////                .lastName("Smith")
////                .email("alice@example.com")
////                .build();
//
//        StudentCourse studentCourse = StudentCourse.builder()
//                .id(1L)
//                .course(courseTest)
//                .student(student)
//                .grade(0.0)
//                .build();
//
//
//        when(teacherRepository.save(Mockito.any(Teacher.class))).thenReturn(teacher);
//        when(courseRepository.findById(courseTest.getId())).thenReturn(Optional.of(courseTest));
//        when(courseRepository.save(courseTest)).thenReturn(courseTest);
//        when(studentService.getStudentById(Mockito.any(UUID.class))).thenReturn(student);
//        when(studentCourseRepository.save(Mockito.any(StudentCourse.class))).thenReturn(studentCourse);
//
//        courseTest = courseRepository.save(courseTest);
//    }
//
//    @Test
//    void shouldReturnCourseWhenIdIsValid() {
//        var courseOptional = courseService.getCourseById(courseTest.getId());
//
//        assertEquals(courseTest.getId(), courseOptional.getId(), "Course Id is not the same");
//    }
//
//    @Test
//    void shouldCreateCourseWhenDtoIsValid() {
//        var dto = new CourseDto(courseTest.getName(), courseTest.getDescription(), courseTest.getMaxAttendees(), courseTest.getTeacher().getId(), new HashSet<>());
//
//        var result = courseService.createCourse(dto);
//
//        assertEquals(modelMapper.map(courseTest,CourseResponseDto.class), result);
//    }
//
//    @Test
//    void shouldApproveStudentForCourse() {
//        StudentCourseResponseDto mockedResponse = new StudentCourseResponseDto();
//        mockedResponse.setCourse(new CourseResponseDto(courseTest.getId(), courseTest.getName(), courseTest.getDescription(), Collections.emptySet()));
//        mockedResponse.setGrade(0.0);
//
//        var studentCourseDto = new StudentCourseDto();
//        studentCourseDto.setCourseId(courseTest.getId());
//        studentCourseDto.setStudentId(student.getId());
//
//        var result = courseService.approveStudent(studentCourseDto);
//
//        assertEquals(mockedResponse, result, "Student should be approved for the course");
//    }
//
//}
//
