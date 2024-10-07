package com.ing.hubs.entity.course;

import com.ing.hubs.entity.course.enrollment.Enrollment;
import com.ing.hubs.entity.course.schedule.Schedule;
import com.ing.hubs.entity.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "course")
@Data
@Builder
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "max_attendees", nullable = false)
    private Integer maxAttendees;

    @ManyToOne
    private Teacher teacher;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany (mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Schedule> schedules = new HashSet<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany (mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Enrollment> enrollments = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany (mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<StudentCourse> studentCourses = new HashSet<>();

    public void setSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.setCourse(this);
    }
}
