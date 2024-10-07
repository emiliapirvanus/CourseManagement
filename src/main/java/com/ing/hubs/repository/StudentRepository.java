package com.ing.hubs.repository;

import com.ing.hubs.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student getStudentByEmail(String userEmail);
}
