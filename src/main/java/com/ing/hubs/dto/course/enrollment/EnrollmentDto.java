package com.ing.hubs.dto.course.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class EnrollmentDto {
    @NotNull
    private UUID courseId;
}
