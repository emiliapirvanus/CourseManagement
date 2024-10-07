package com.ing.hubs.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class StudentCourseDto {
    @NotNull
    private UUID studentId;
    @NotNull
    private UUID courseId;
}
