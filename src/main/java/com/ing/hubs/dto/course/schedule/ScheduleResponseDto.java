package com.ing.hubs.dto.course.schedule;

import com.ing.hubs.entity.course.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ScheduleResponseDto {
    private CourseType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
}
