package com.ing.hubs.dto.teacher;

import com.ing.hubs.entity.teacher.TeacherTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TeacherResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private TeacherTitle teacherTitle;
    private LocalDate joinDate;
    private String expertiseArea;
}
