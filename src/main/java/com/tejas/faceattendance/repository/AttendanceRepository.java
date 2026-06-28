package com.tejas.faceattendance.repository;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    boolean existsByStudentAndAttendanceDate(Student student, LocalDate attendanceDate);

}