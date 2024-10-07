package com.ing.hubs.repository;

import com.ing.hubs.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    Teacher getTeacherByEmail(String userEmail);
}
