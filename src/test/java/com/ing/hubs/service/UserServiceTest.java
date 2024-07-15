package com.ing.hubs.service;

import com.ing.hubs.dto.JwtDto;
import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.UserResetPasswordDto;
import com.ing.hubs.dto.UserSessionDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.teacher.TeacherDto;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.UserRole;
import com.ing.hubs.exception.user.PasswordIncorrectException;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.repository.UserRepository;
import com.ing.hubs.security.JwtDecoder;
import com.ing.hubs.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserService userService;


    @Test
    void shouldSaveStudentEntity() {

        StudentDto studentDto = new StudentDto();
        studentDto.setEmail("epirvanus@gmail.com");
        studentDto.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveStudent(studentDto);

        verify(userRepository, times(1)).save(argThat(user ->
                "epirvanus@gmail.com".equals(user.getEmail())
                        && "encodedPassword".equals(user.getPassword())
                        && UserRole.STUDENT.equals(user.getRole())));
    }

    @Test
    void shouldSaveTeacherEntity() {

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setEmail("teacher@gmail.com");
        teacherDto.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveTeacher(teacherDto);

        verify(userRepository, times(1)).save(argThat(user ->
                "teacher@gmail.com".equals(user.getEmail())
                        && "encodedPassword".equals(user.getPassword())
                        && UserRole.TEACHER.equals(user.getRole())));
    }

    @Test
    public void shouldCallFindByIdMethod() {

        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.getById(userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnUserWhenUserIdExists() {

        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getById(userId);

        assertEquals(mockUser, result);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesNotExist() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    void shouldCreateSessionAndReturnJwtToken() {

        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setEmail("user@gmail.com");
        userSessionDto.setPassword("password");

        User mockUser = new User();
        mockUser.setEmail(userSessionDto.getEmail());
        mockUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(userSessionDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtProvider.generateJwt(mockUser)).thenReturn("mockJwtToken");

        JwtDto result = userService.createSession(userSessionDto);

        assertNotNull(result);
        assertEquals("mockJwtToken", result.getJwt());

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(userSessionDto.getEmail(), userSessionDto.getPassword()));

        verify(userRepository, times(1)).findByEmail(userSessionDto.getEmail());

        verify(jwtProvider, times(1)).generateJwt(mockUser);
    }


    @Test
    void shouldThrowUserNotFoundExceptionForInvalidSession() {
        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setEmail("nonexistent@gmail.com");
        userSessionDto.setPassword("password");

        when(userRepository.findByEmail(userSessionDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.createSession(userSessionDto));

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(userSessionDto.getEmail(), userSessionDto.getPassword()));

        verify(userRepository, times(1)).findByEmail(userSessionDto.getEmail());

        verifyNoInteractions(jwtProvider);
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {

        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserResetPasswordDto resetPasswordDto = new UserResetPasswordDto();
        resetPasswordDto.setOldPassword(oldPassword);
        resetPasswordDto.setNewPassword(newPassword);

        String sessionEmail = "user@example.com";
        when(jwtDecoder.getSessionEmail()).thenReturn(sessionEmail);

        User existingUser = new User();
        existingUser.setEmail(sessionEmail);
        existingUser.setPassword(passwordEncoder.encode(oldPassword));

        when(userRepository.findByEmail(sessionEmail)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(eq(oldPassword), any())).thenReturn(true);

        StringResponseDto responseDto = userService.updatePassword(resetPasswordDto);

        assertEquals("Password successfully changed", responseDto.getMessage());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldThrowPasswordIncorrectExceptionWhenWrongOldPassword() {

        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserResetPasswordDto resetPasswordDto = new UserResetPasswordDto();
        resetPasswordDto.setOldPassword(oldPassword);
        resetPasswordDto.setNewPassword(newPassword);

        String sessionEmail = "user@example.com";
        when(jwtDecoder.getSessionEmail()).thenReturn(sessionEmail);

        User existingUser = new User();
        existingUser.setEmail(sessionEmail);
        existingUser.setPassword(passwordEncoder.encode("differentPassword"));

        when(userRepository.findByEmail(sessionEmail)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(eq(oldPassword), any())).thenReturn(false);

        assertThrows(PasswordIncorrectException.class, () -> userService.updatePassword(resetPasswordDto));
        verify(passwordEncoder, never()).encode(newPassword);
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {

        String sessionEmail = "nonexistent@example.com";
        when(jwtDecoder.getSessionEmail()).thenReturn(sessionEmail);
        when(userRepository.findByEmail(sessionEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(new UserResetPasswordDto()));
    }

}