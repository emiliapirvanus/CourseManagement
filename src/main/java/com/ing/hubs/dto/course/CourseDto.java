package com.ing.hubs.dto.course;

import com.ing.hubs.entity.course.schedule.Schedule;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CourseDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Min(value = 1)
    private Integer maxAttendees;

    private UUID teacherId;
    private Set<Schedule> schedules;
}
