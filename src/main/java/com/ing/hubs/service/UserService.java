package com.ing.hubs.service;


import com.ing.hubs.dto.JwtDto;
import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.UserResetPasswordDto;
import com.ing.hubs.dto.UserSessionDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.teacher.TeacherDto;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.UserRole;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.exception.user.PasswordIncorrectException;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.repository.UserRepository;
import com.ing.hubs.security.JwtDecoder;
import com.ing.hubs.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private JwtDecoder jwtDecoder;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public void saveStudent(StudentDto studentDto) {
        var userEntity = User.builder()
                .email(studentDto.getEmail())
                .password(passwordEncoder.encode(studentDto.getPassword()))
                .role(UserRole.STUDENT)
                .build();
        userRepository.save(userEntity);
    }

    public void saveTeacher(TeacherDto teacherDto) {
        var userEntity = User.builder()
                .email(teacherDto.getEmail())
                .password(passwordEncoder.encode(teacherDto.getPassword()))
                .role(UserRole.TEACHER)
                .build();
        userRepository.save(userEntity);
    }

    public JwtDto createSession(UserSessionDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        String jwt = jwtProvider.generateJwt(user);

        return new JwtDto(jwt);
    }

    public StringResponseDto updatePassword(UserResetPasswordDto dto) {

        var user = userRepository.findByEmail(jwtDecoder.getSessionEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new PasswordIncorrectException();
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return new StringResponseDto("Password successfully changed");
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public void updateEmail(String email) {
        var user = userRepository.findByEmail(jwtDecoder.getSessionEmail()).orElseThrow(UserNotFoundException::new);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void deleteUser(Student student) {
        var user = userRepository.findByEmail(student.getEmail()).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}