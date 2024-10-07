package com.ing.hubs.service;

import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentIdWrapper;
import com.ing.hubs.dto.course.enrollment.EnrollmentResponseDto;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import com.ing.hubs.entity.student.Student;
import com.ing.hubs.exception.course.CourseLimitReachedException;
import com.ing.hubs.exception.course.ScheduleConflictException;
import com.ing.hubs.exception.enrollment.AlreadyEnrolledInCourseException;
import com.ing.hubs.exception.enrollment.EnrollmentNotFound;
import com.ing.hubs.exception.enrollment.PendingEnrollmentConflictException;
import com.ing.hubs.repository.EnrollmentRepository;
import com.ing.hubs.security.JwtDecoder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnrollmentService {
    private EnrollmentRepository enrollmentRepository;
    private StudentService studentService;
    private CourseService courseService;
    private JwtDecoder jwtDecoder;
    private ModelMapper modelMapper;

    public List<EnrollmentResponseDto> getEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponseDto> getPendingEnrollments() {
        var enrollments = enrollmentRepository
                .findAllByCourseTeacherId(jwtDecoder.getSessionUserId())
                .orElseThrow(EnrollmentNotFound::new)
                .stream()
                .filter(enrollment -> enrollment.getStatus().equals(EnrollmentStatus.PENDING))
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentResponseDto.class))
                .collect(Collectors.toList());

        if (enrollments.isEmpty()) {
            throw new EnrollmentNotFound();
        }

        return enrollments;
    }

    public List<EnrollmentResponseDto> getSessionEnrollments() {
        var enrollments = enrollmentRepository
                .findAllByStudentId(jwtDecoder.getSessionUserId())
                .orElseThrow(EnrollmentNotFound::new)
                .stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentResponseDto.class))
                .collect(Collectors.toList());;

        if(enrollments.isEmpty()) {
            throw new EnrollmentNotFound();
        }

        return enrollments;
    }

    public EnrollmentIdWrapper save(EnrollmentDto enrollmentDto) {
        var student = studentService.getStudentById(jwtDecoder.getSessionUserId());
        var course = courseService.getCourseById(enrollmentDto.getCourseId());

        boolean hasExistingEnrollment = student.getEnrollments().stream()
                .anyMatch(enrollment ->
                        enrollment.getCourse().getId().equals(course.getId())
                                && enrollment.getStatus() == EnrollmentStatus.PENDING);

        boolean isAlreadyEnrolledInCourse = student.getStudentCourses().stream()
                .anyMatch(c ->
                        c.getCourse().getId().equals(course.getId()));

        if (isAlreadyEnrolledInCourse) {
            throw new AlreadyEnrolledInCourseException();
        }

        if (hasExistingEnrollment) {
            throw new PendingEnrollmentConflictException();
        }

        if (hasScheduleConflict(student, course)) {
            throw new ScheduleConflictException();
        }
        if(checkCourseLimitExceeded(course)) {
            throw new CourseLimitReachedException();
        }

        var enrollment = Enrollment.builder().build();
        student.joinCourse(enrollment, course);

        return new EnrollmentIdWrapper(enrollmentRepository.save(enrollment).getId());
    }

    @Transactional
    public StringResponseDto deleteEnrollment(EnrollmentIdWrapper enrollmentIdWrapper) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentIdWrapper.getEnrollmentId())
                .orElseThrow(EnrollmentNotFound::new);

        if(enrollment.getStatus().equals(EnrollmentStatus.ACCEPTED)) {
            return new StringResponseDto("Enrollment cannot be deleted because it's already accepted");
        }

        enrollmentRepository.delete(enrollment);

        return new StringResponseDto("Enrollment deleted: " + enrollmentIdWrapper.getEnrollmentId());
    }

    @Transactional
    public void denyOldPendingEnrollments() {
        var thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        var oldPendingEnrollments = enrollmentRepository
                .findAllByStatusAndEnrolledAtBefore(EnrollmentStatus.PENDING, thirtyDaysAgo);

        for (var enrollment : oldPendingEnrollments) {
            enrollment.setStatus(EnrollmentStatus.DENIED);
            enrollment.setUpdatedAt(LocalDateTime.now());
            enrollmentRepository.save(enrollment);
        }
    }

    private boolean checkCourseLimitExceeded(Course course) {
        return course.getStudentCourses().size() >= course.getMaxAttendees();
    }

    private boolean hasScheduleConflict(Student student, Course course) {
        var courseSchedules = course.getSchedules();
        var studentSchedules = student.getStudentCourses()
                .stream()
                .flatMap(studentCourse -> studentCourse.getCourse().getSchedules().stream())
                .collect(Collectors.toSet());

        var pendingEnrollmentSchedules = student.getEnrollments().stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.PENDING)
                .flatMap(enrollment -> enrollment.getCourse().getSchedules().stream())
                .collect(Collectors.toList());

        studentSchedules.addAll(pendingEnrollmentSchedules);

        for (var studentSchedule : studentSchedules) {
            for (var courseSchedule : courseSchedules) {
                boolean isSameWeekday = studentSchedule.getWeekDay() == courseSchedule.getWeekDay();
                boolean isOverlappingDates = studentSchedule.getStartDate().isBefore(courseSchedule.getEndDate()) &&
                        studentSchedule.getEndDate().isAfter(courseSchedule.getStartDate());
                boolean isOverlappingTimes = studentSchedule.getStartTime().isBefore(courseSchedule.getEndTime()) &&
                        studentSchedule.getEndTime().isAfter(courseSchedule.getStartTime());

                if (isSameWeekday && isOverlappingDates && isOverlappingTimes) {
                    return true;
                }
            }
        }
        return false;
    }


}
