package com.ing.hubs.entity.course;

import com.ing.hubs.entity.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "student_course")
@Data
@Builder
@AllArgsConstructor
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Course course;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Student student;

    @Column
    private double grade;

    @Column (name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
