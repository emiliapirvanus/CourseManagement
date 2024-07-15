package com.ing.hubs.service;

import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.dto.student.StudentUpdateDto;
import com.ing.hubs.entity.student.Location;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.exception.user.UserUpdateEmailException;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.exception.user.UserRegisteredException;
import com.ing.hubs.repository.LocationRepository;
import com.ing.hubs.repository.StudentCourseRepository;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.security.JwtDecoder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;
    private LocationRepository locationRepository;
    private ModelMapper modelMapper;
    private UserService userService;
    private JwtDecoder jwtDecoder;
//    private StudentCourseRepository studentCourseRepository;

    public List<StudentResponseDto> getStudents() {
        var students = studentRepository.findAll();
        return students.stream()
                .map(student -> modelMapper.map(student, StudentResponseDto.class))
                .collect(Collectors.toList());
    }

    public StudentResponseDto getStudentFromSession() {
        var studentId = jwtDecoder.getSessionUserId();
        var student = studentRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(student, StudentResponseDto.class);
    }
    public StudentResponseDto getStudentResponseById(UUID studentId) {
        return modelMapper
                .map(studentRepository
                        .findById(studentId)
                        .orElseThrow(UserNotFoundException::new),
                        StudentResponseDto.class);
    }
    public Student getStudentById(UUID studentId) {
        return studentRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
    }

    public StudentResponseDto addStudent(StudentDto studentDto) {
        try {
            if (studentDto == null || studentDto.getLocation() == null) {
                throw new IllegalArgumentException("StudentDto or Location cannot be null");
            }

            var studentEntity = modelMapper.map(studentDto, Student.class);
            var location = modelMapper.map(studentDto.getLocation(), Location.class);

            if (studentEntity == null || location == null) {
                throw new IllegalStateException("Mapping from StudentDto to Student or Location failed");
            }

            var savedLocation = locationRepository.save(location);
            studentEntity.setLocation(savedLocation);
            userService.saveStudent(studentDto);

            return modelMapper.map(
                    studentRepository.save(studentEntity),
                    StudentResponseDto.class);

        } catch (DataIntegrityViolationException ex) {
            throw new UserRegisteredException();
        }
    }


    @Transactional
    public StudentResponseDto updateStudent(StudentUpdateDto studentUpdateDto) {
        var student = studentRepository.findById(jwtDecoder.getSessionUserId())
                .orElseThrow(UserNotFoundException::new);

        if (studentUpdateDto.getPhoneNumber() != null) {
            student.setPhoneNumber(studentUpdateDto.getPhoneNumber());
        }

        if(studentUpdateDto.getEmail().matches(student.getEmail())) {
            throw new UserUpdateEmailException();
        }

        if (studentUpdateDto.getEmail() != null) {
            student.setEmail(studentUpdateDto.getEmail());
            userService.updateEmail(studentUpdateDto.getEmail());
        }

        if (studentUpdateDto.getLocation() != null) {
            var location = modelMapper.map(
                    studentUpdateDto.getLocation(),
                    Location.class);
            var savedLocation = locationRepository.save(location);
            student.setLocation(savedLocation);
        }

        return modelMapper.map(
                studentRepository.save(student),
                StudentResponseDto.class);
    }

//    @Transactional
//    public StringResponseDto deleteStudent() {
//        var student = studentRepository.findById(jwtDecoder.getSessionUserId()).orElseThrow(UserNotFoundException::new);
//        studentCourseRepository.deleteAllByStudentId(student.getId());
//        userService.deleteUser(student);
//        studentRepository.delete(student);
//        return new StringResponseDto("User deleted");
//    }
}
