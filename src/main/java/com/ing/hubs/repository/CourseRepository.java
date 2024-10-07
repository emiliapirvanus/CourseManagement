package com.ing.hubs.repository;

import com.ing.hubs.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findAllByTeacherId(UUID teacherId);
    Optional<Course> findByName(String name);
}
