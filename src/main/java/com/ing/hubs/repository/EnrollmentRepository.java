package com.ing.hubs.repository;

import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    Optional<List<Enrollment>> findAllByStudentId(UUID studentId);
    Optional<List<Enrollment>> findAllByCourseTeacherId(UUID teacherId);

    List<Enrollment> findAllByStatusAndEnrolledAtBefore(EnrollmentStatus enrollmentStatus, LocalDateTime thirtyDaysAgo);
}
