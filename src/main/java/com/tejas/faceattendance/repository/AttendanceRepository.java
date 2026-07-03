package com.tejas.faceattendance.repository;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Check if attendance already exists for a student today
    Optional<Attendance> findByStudentAndAttendanceDate(Student student,
                                                        LocalDate attendanceDate);

    // Get all attendance records of a student
    List<Attendance> findByStudent(Student student);

    // Get today's attendance
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
}