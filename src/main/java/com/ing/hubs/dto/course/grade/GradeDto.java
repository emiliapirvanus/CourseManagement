package com.ing.hubs.dto.course.grade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDto {
    @NotNull
    private UUID studentId;
    @NotNull
    private UUID courseId;
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private double grade;
}
