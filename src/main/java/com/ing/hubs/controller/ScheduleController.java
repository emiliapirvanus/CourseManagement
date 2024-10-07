package com.ing.hubs.controller;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.course.schedule.ScheduleDto;
import com.ing.hubs.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/schedules")
public class ScheduleController {
    private ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CourseResponseDto createSchedule(@Valid @RequestBody ScheduleDto dto) {
        return scheduleService.createSchedule(dto);
    }
}
