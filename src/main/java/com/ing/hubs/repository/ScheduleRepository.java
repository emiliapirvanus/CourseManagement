package com.ing.hubs.repository;

import com.ing.hubs.entity.course.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Set<Schedule> findAllByCourseTeacherId(UUID id);
}
