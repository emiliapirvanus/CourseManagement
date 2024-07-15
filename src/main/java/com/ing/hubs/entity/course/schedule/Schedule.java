package com.ing.hubs.entity.course.schedule;

import com.ing.hubs.entity.course.Course;
import com.ing.hubs.entity.course.CourseType;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column (name = "course_type", nullable = false)
    @Enumerated (value = EnumType.STRING)
    private CourseType type;
    @Column (name = "start_date")
    @Builder.Default
    private LocalDate startDate = LocalDate.now();
    @Column (name = "end_date")
    private LocalDate endDate;
    @Column (name = "week_day")
    private DayOfWeek weekDay;
    @Column (name = "start_time")
    private LocalTime startTime;
    @Column (name = "end_time")
    private LocalTime endTime;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Course course;
}
