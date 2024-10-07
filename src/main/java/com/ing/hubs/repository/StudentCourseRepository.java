package com.ing.hubs.repository;

import com.ing.hubs.entity.course.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, UUID> {

    @Query("SELECT sc FROM StudentCourse sc WHERE sc.course.id = :courseId AND sc.student.id = :studentId")
    Optional<StudentCourse> findByCourseIdAndStudentId(UUID courseId, UUID studentId);

    void deleteAllByStudentId(UUID studentId);

}
