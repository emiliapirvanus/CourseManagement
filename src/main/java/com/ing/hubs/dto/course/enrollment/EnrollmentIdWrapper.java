package com.ing.hubs.dto.course.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentIdWrapper {
    private UUID enrollmentId;
}
