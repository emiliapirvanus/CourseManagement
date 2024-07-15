package com.ing.hubs.service;

import com.ing.hubs.dto.course.CourseResponseDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentDto;
import com.ing.hubs.dto.course.enrollment.EnrollmentResponseDto;
import com.ing.hubs.dto.course.schedule.ScheduleDto;
import com.ing.hubs.dto.student.LocationDto;
import com.ing.hubs.dto.student.StudentDto;
import com.ing.hubs.dto.student.StudentResponseDto;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.CourseType;
import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import com.ing.hubs.entity.course.schedule.Schedule;
import com.ing.hubs.entity.student.Gender;
import com.ing.hubs.entity.student.Location;
import com.ing.hubs.entity.student.Student;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestDataUtil {

    private TestDataUtil() {
    }

    public static StudentDto createMockStudentDto() {
        return StudentDto.builder()
                .firstName("Emilia")
                .lastName("Pirvanus")
                .phoneNumber("+40752567890")
                .email("epirvanus@gmail.com")
                .password("password")
                .dateOfBirth(LocalDate.of(2001, 3, 24))
                .gender(Gender.FEMALE)
                .location(createMockLocationDto())
                .build();
    }

    public static StudentResponseDto createStudentResponseDto() {
        UUID studentId = UUID.randomUUID();

        return StudentResponseDto.builder()
                .id(studentId)
                .firstName("Emilia")
                .lastName("Pirvanus")
                .phoneNumber("+40752567890")
                .email("epirvanus@gmail.com")
                .dateOfBirth(LocalDate.of(2001, 3, 24))
                .gender(Gender.FEMALE)
                .location(createMockLocationDto())
                .build();
    }

    public static LocationDto createMockLocationDto() {
        return LocationDto.builder()
                .streetAddress("Alexandru Ioan Cuza")
                .city("Bucharest")
                .postalCode("12345")
                .country("Romania")
                .build();
    }

    public static StudentResponseDto createMockStudentResponseDto(StudentDto studentDto) {
        return StudentResponseDto.builder()
                .id(UUID.randomUUID())
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .phoneNumber(studentDto.getPhoneNumber())
                .dateOfBirth(studentDto.getDateOfBirth())
                .gender(studentDto.getGender())
                .location(createMockLocationDto())
                .build();
    }

    public static Student createMockStudentEntity(StudentDto studentDto, Location location) {
        return Student.builder()
                .id(UUID.randomUUID())
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .phoneNumber(studentDto.getPhoneNumber())
                .email(studentDto.getEmail())
                .dateOfBirth(studentDto.getDateOfBirth())
                .gender(studentDto.getGender())
                .location(location)
                .build();
    }

    public static Location createMockLocationEntity(LocationDto locationDto) {
        return Location.builder()
                .id(UUID.randomUUID())
                .streetAddress(locationDto.getStreetAddress())
                .city(locationDto.getCity())
                .postalCode(locationDto.getPostalCode())
                .country(locationDto.getCountry())
                .build();
    }

    public static ScheduleDto createScheduleDto() {
        return ScheduleDto.builder()
                .courseId(UUID.randomUUID())
                .type(CourseType.COURSE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .weekDay(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .build();
    }

    public static Schedule createScheduleEntity(ScheduleDto scheduleDto) {
        return Schedule.builder()
                .id(UUID.randomUUID())
                .type(scheduleDto.getType())
                .startDate(scheduleDto.getStartDate())
                .endDate(scheduleDto.getEndDate())
                .weekDay(scheduleDto.getWeekDay())
                .startTime(scheduleDto.getStartTime())
                .endTime(scheduleDto.getEndTime())
                .build();
    }

    public static Course createCourseEntity() {
        return Course.builder()
                .id(UUID.randomUUID())
                .name("OOP")
                .description("Learning OOP")
                .schedules(new HashSet<>())
                .build();
    }

    public static CourseResponseDto createCourseResponseDto(Course courseEntity) {
        return new CourseResponseDto(
                courseEntity.getId(),
                courseEntity.getName(),
                courseEntity.getDescription(),
                Collections.emptySet()
        );
    }

    public static CourseResponseDto createCourseResponseDto() {
        return CourseResponseDto.builder()
                .id(UUID.randomUUID())
                .name("OOP")
                .description("Learning OOP")
                .schedules(Collections.emptySet())
                .build();

    }

    public static EnrollmentResponseDto createEnrollmentResponseDto(Enrollment enrollment, StudentResponseDto studentResponseDto, CourseResponseDto courseResponseDto) {
        return EnrollmentResponseDto.builder()
                .course(courseResponseDto)
                .student(studentResponseDto)
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }

    public static Enrollment createEnrollment(Student student, Course course) {
        return Enrollment.builder()
                .course(course)
                .student(student)
                .status(EnrollmentStatus.PENDING)
                .build();
    }

}
