package com.ing.hubs.service;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.course.schedule.ScheduleDto;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.schedule.Schedule;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.course.ScheduleConflictException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.ScheduleRepository;
import com.ing.hubs.security.JwtDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private ScheduleService scheduleService = new ScheduleService(scheduleRepository, courseRepository, new ModelMapper(), jwtDecoder);

    private ScheduleDto scheduleDto;
    private Schedule newSchedule;
    private Course course;
    protected CourseResponseDto courseResponseDto;
    Set<Schedule> scheduleSet = new HashSet<>();

    @BeforeEach
    public void setUp() {

        scheduleDto = TestDataUtil.createScheduleDto();
        newSchedule = TestDataUtil.createScheduleEntity(scheduleDto);
        newSchedule.setId(UUID.randomUUID());
        course = TestDataUtil.createCourseEntity();
        courseResponseDto = TestDataUtil.createCourseResponseDto(course);

        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();

        scheduleSet.add(schedule1);
        scheduleSet.add(schedule2);
    }

    @Test
    void shouldCreateScheduleWhenOk() {

        when(courseRepository.findById(scheduleDto.getCourseId())).thenReturn(Optional.of(course));
        when(scheduleRepository.findAllByCourseTeacherId(any())).thenReturn(Set.of());
        when(scheduleRepository.save(any())).thenReturn(newSchedule);
        when(courseRepository.save(course)).thenReturn(course);

        CourseResponseDto result = scheduleService.createSchedule(scheduleDto);

        verify(scheduleRepository, times(1)).save(any());
        verify(courseRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowScheduleConflictExceptionWhenScheduleConflict() {

        when(courseRepository.findById(scheduleDto.getCourseId())).thenReturn(Optional.of(course));
        when(scheduleRepository.findAllByCourseTeacherId(any())).thenReturn(Set.of(newSchedule));

        assertThrows(ScheduleConflictException.class, () -> scheduleService.createSchedule(scheduleDto));
    }


    @Test
    public void shouldThrowCourseNotFoundExceptionWhenCourseNotFound() {

        when(courseRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> scheduleService.createSchedule(scheduleDto));
    }
}