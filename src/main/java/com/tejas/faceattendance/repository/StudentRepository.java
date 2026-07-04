package com.tejas.faceattendance.repository;

import com.tejas.faceattendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find Student by USN
    Optional<Student> findByUsn(String usn);

    // ==========================================
    // Department-wise Student Count
    // ==========================================

    @Query("""
            SELECT s.department, COUNT(s)
            FROM Student s
            GROUP BY s.department
            """)
    List<Object[]> getDepartmentWiseStudentCount();

}