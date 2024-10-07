package com.ing.hubs.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseResponseDto {
    private CourseResponseDto course;
    private double grade;
    private LocalDateTime updatedAt;
}
