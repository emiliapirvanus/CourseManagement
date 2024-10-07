package com.ing.hubs.entity.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ing.hubs.entity.Person;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.StudentCourse;
import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.enrollment.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "student")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class Student extends Person {
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    @Enumerated (value = EnumType.STRING)
    private Gender gender;


    @OneToOne
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Location location;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonIgnore
    @OneToMany (mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Enrollment> enrollments = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonIgnore
    @OneToMany (mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    public void joinCourse(Enrollment enrollment, Course course) {
        this.enrollments.add(enrollment);
        enrollment.setStudent(this);
        course.getEnrollments().add(enrollment);
        enrollment.setCourse(course);
    }

    public Optional<Enrollment> findPendingEnrollment(UUID courseId, UUID teacherId) {
        return this.enrollments.stream()
                .filter(enrollment -> enrollment.getStatus().equals(EnrollmentStatus.PENDING)
                        && enrollment.getCourse().getId().equals(courseId)
                        && enrollment.getCourse().getTeacher().getId().equals(teacherId))
                .findFirst();
    }
}
