package com.ing.hubs.service;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.course.schedule.ScheduleDto;
import com.ing.hubs.entity.course.schedule.Schedule;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.course.ScheduleConflictException;
import com.ing.hubs.exception.course.ScheduleDateInPastException;
import com.ing.hubs.exception.course.ScheduleStartDateAfterEndDateException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.ScheduleRepository;
import com.ing.hubs.security.JwtDecoder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@AllArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private CourseRepository courseRepository;
    private ModelMapper modelMapper;
    private JwtDecoder jwtDecoder;

    public CourseResponseDto createSchedule(ScheduleDto dto) {
        var course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(CourseNotFoundException::new);

        var existingSchedules = scheduleRepository.findAllByCourseTeacherId(jwtDecoder.getSessionUserId());

        var newSchedule = modelMapper.map(dto, Schedule.class);

        if(hasScheduleConflict(newSchedule, existingSchedules)) {
            throw new ScheduleConflictException();
        }

        if (!isStartAndEndDateMismatched(newSchedule)) {
            throw new ScheduleStartDateAfterEndDateException();
        }

        if(isStartDateOrEndDateInPast(newSchedule)) {
            throw new ScheduleDateInPastException();
        }

        course.setSchedule(scheduleRepository.save(newSchedule));
        return modelMapper.map(
                courseRepository.save(course),
                CourseResponseDto.class);
    }

    private boolean isStartDateOrEndDateInPast(Schedule newSchedule) {
        return newSchedule.getStartDate().isBefore(LocalDate.now()) ||
                newSchedule.getEndDate().isBefore(LocalDate.now());
    }

    private boolean hasScheduleConflict(Schedule newSchedule, Set<Schedule> existingSchedules) {
        for (var existingSchedule : existingSchedules) {
            if(newSchedule.getWeekDay() == existingSchedule.getWeekDay()
                    && newSchedule.getStartTime().isBefore(existingSchedule.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStartAndEndDateMismatched(Schedule newSchedule) {
        return newSchedule.getStartDate().isBefore(newSchedule.getEndDate());
    }
}
