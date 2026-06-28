package com.tejas.faceattendance.repository;

import com.tejas.faceattendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUsn(String usn);

}