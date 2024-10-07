package com.ing.hubs.security;

import com.ing.hubs.entity.User;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;
@AllArgsConstructor
@Component
public class JwtDecoder {
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String ROLE_TEACHER = "ROLE_TEACHER";

    public UUID getSessionUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User userPrincipal) {

            var userEmail = userPrincipal.getUsername();
            var userRole = getUserRole(userPrincipal);

            if (userRole.equals(ROLE_STUDENT)) {
                var student = studentRepository.getStudentByEmail(userEmail);
                return student.getId();
            } else if (userRole.equals(ROLE_TEACHER)) {
                var teacher = teacherRepository.getTeacherByEmail(userEmail);
                return teacher.getId();
            }
        }

        throw new UserNotFoundException();
    }

    public String getSessionEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User userPrincipal) {

            return userPrincipal.getUsername();
        }

        throw new UserNotFoundException();
    }

    private static String getUserRole(User userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("");
    }

}
