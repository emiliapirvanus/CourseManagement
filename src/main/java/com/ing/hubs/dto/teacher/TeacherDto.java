package com.ing.hubs.dto.teacher;

import com.ing.hubs.entity.teacher.TeacherTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TeacherDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;
    @NotBlank
    private String password;
    private TeacherTitle teacherTitle;
    @NotBlank
    private String expertiseArea;
}
