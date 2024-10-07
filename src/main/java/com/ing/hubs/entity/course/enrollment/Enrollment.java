package com.ing.hubs.entity.course.enrollment;

import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "enrollment")
@Data
@Builder
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Course course;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Student student;

    @Column (name = "enrollment_status")
    @Enumerated (value = EnumType.STRING)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column( name = "enrolled_at")
    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
