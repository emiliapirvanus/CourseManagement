package com.ing.hubs.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CourseUpdateDto {
    private String name;
    private String description;
    private Integer maxAttendees;
    @NotNull
    private UUID courseId;
}
