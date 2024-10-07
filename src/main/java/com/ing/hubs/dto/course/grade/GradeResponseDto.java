package com.ing.hubs.dto.course.grade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GradeResponseDto {
    private UUID studentId;
    private UUID courseId;
    private double grade;
}
