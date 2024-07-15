package com.ing.hubs.service;

import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.dto.student.StudentUpdateDto;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.UserRole;
import com.ing.hubs.entity.course.StudentCourse;
import com.ing.hubs.entity.student.Location;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.repository.LocationRepository;
import com.ing.hubs.repository.StudentCourseRepository;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.repository.UserRepository;
import com.ing.hubs.security.JwtDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentCourseRepository studentCourseRepository;
    @InjectMocks
    private StudentService studentService = new StudentService(studentRepository, locationRepository, new ModelMapper(), userService, jwtDecoder);

    @Mock
    private static BCryptPasswordEncoder passwordEncoder;
    private Student mockStudent;
    private StudentDto mockStudentDto;
    private Location mockLocation;
    private StudentResponseDto mockStudentResponseDto;
    private User userEntity;

    @BeforeEach
    public void setUp() {
        mockStudentDto = TestDataUtil.createMockStudentDto();
        mockLocation = TestDataUtil.createMockLocationEntity(mockStudentDto.getLocation());
        mockStudent = TestDataUtil.createMockStudentEntity(mockStudentDto, mockLocation);
        mockStudentResponseDto = TestDataUtil.createMockStudentResponseDto(mockStudentDto);
        userEntity = User.builder()
                .id(UUID.randomUUID())
                .email(mockStudentDto.getEmail())
                .password(passwordEncoder.encode(mockStudentDto.getPassword()))
                .role(UserRole.STUDENT)
                .build();
    }

    @Test
    public void shouldCallRepoMethod() {

        studentService.getStudents();

        verify(studentRepository).findAll();
    }


    @Test
    public void shouldReturnAllStudentsWhenStudentsExist() {
        List<Student> students = List.of(new Student(), new Student());

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentResponseDto> studentResponseList = studentService.getStudents();

        assertNotNull(studentResponseList);
        assertEquals(2, studentResponseList.size());
    }

    @Test
    public void shouldGetStudents() {

        List<Student> mockStudents = List.of(
                new Student(),
                new Student()
        );

        when(studentRepository.findAll()).thenReturn(mockStudents);

        ModelMapper modelMapper = new ModelMapper();

        List<StudentResponseDto> expectedDtos = mockStudents.stream()
                .map(student -> modelMapper.map(student, StudentResponseDto.class))
                .collect(Collectors.toList());

        List<StudentResponseDto> actualDtos = studentService.getStudents();

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    void shouldReturnStudentFromSession() {

        UUID studentId = UUID.randomUUID();

        Student mockedStudent = new Student();
        mockedStudent.setId(studentId);

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockedStudent));

        StudentResponseDto studentResponseDto = StudentResponseDto.builder().id(studentId).build();

        StudentResponseDto result = studentService.getStudentFromSession();

        assertEquals(studentResponseDto, result);
    }

    @Test
    void shouldReturnStudentWhenStudentIdFound() {
        UUID studentId = UUID.randomUUID();

        Student mockedStudent = new Student();
        mockedStudent.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockedStudent));

        Student result = studentService.getStudentById(studentId);

        assertEquals(mockedStudent, result);

    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenStudentIdNotFound() {

        UUID studentId = UUID.randomUUID();

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studentService.getStudentFromSession());
    }

    @Test
    public void shouldAddStudentSuccessfully() {

        when(locationRepository.save(any())).thenReturn(mockLocation);
        when(studentRepository.save(any())).thenReturn(mockStudent);

        StudentResponseDto result = studentService.addStudent(mockStudentDto);

        assertNotNull(result.getId());

        assertEquals(mockStudentDto.getFirstName(), result.getFirstName());
        assertEquals(mockStudentDto.getLastName(), result.getLastName());

        verify(studentRepository, times(1)).save(any(Student.class));
        verify(locationRepository, times(1)).save(any(Location.class));

    }

    @Test
    void shouldUpdateStudent() {

        UUID studentId = UUID.randomUUID();
        StudentUpdateDto studentUpdateDto = new StudentUpdateDto();
        studentUpdateDto.setEmail("newemail@example.com");

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setEmail("oldemail@example.com");

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));


        var updatedStudentResponseDto = studentService.updateStudent(studentUpdateDto);

        assertEquals(studentUpdateDto.getEmail(), updatedStudentResponseDto.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void ShouldThrowUserNotFoundExceptionWhenUserNotFound() {

        UUID studentId = UUID.randomUUID();
        StudentUpdateDto studentUpdateDto = new StudentUpdateDto();

        when(jwtDecoder.getSessionUserId()).thenReturn(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studentService.updateStudent(studentUpdateDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

}