package com.ing.hubs.entity.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ing.hubs.entity.Person;
import com.ing.hubs.entity.User;
import com.ing.hubs.entity.course.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "teacher")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class Teacher extends Person {
    @Column(name = "teacher_title")
    @Builder.Default
    @Enumerated (value = EnumType.STRING)
    private TeacherTitle teacherTitle = TeacherTitle.ASSOCIATE_PROFESSOR;
    @Column(name = "join_date")
    @Builder.Default
    private LocalDate joinDate = LocalDate.now();
    @Column(name = "expertise_area")
    private String expertiseArea;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }
}
