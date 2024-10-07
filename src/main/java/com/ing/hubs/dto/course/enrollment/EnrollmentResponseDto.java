package com.ing.hubs.dto.course.enrollment;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseDto {
    private StudentResponseDto student;
    private CourseResponseDto course;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime updatedAt;
}
