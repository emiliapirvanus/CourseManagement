package com.ing.hubs.dto.course;

import com.ing.hubs.dto.course.schedule.ScheduleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class CourseResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Set<ScheduleResponseDto> schedules;
}
