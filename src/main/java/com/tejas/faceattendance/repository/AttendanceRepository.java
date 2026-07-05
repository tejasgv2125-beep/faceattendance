package com.tejas.faceattendance.repository;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ==========================================
    // Check Today's Attendance
    // ==========================================

    Optional<Attendance> findByStudentAndAttendanceDate(
            Student student,
            LocalDate attendanceDate
    );

    // ==========================================
    // Student Attendance
    // ==========================================

    List<Attendance> findByStudent(Student student);
    List<Attendance> findTop5ByOrderByAttendanceDateDescAttendanceTimeDesc();
    List<Attendance> findByStudentOrderByAttendanceDateDescAttendanceTimeDesc(
            Student student
    );

    long countByStudent(Student student);

    long countByStudentAndStatus(
            Student student,
            String status
    );

    // ==========================================
    // Date
    // ==========================================

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
    // ==========================================
// All Attendance (Latest First)
// ==========================================

    List<Attendance> findAllByOrderByAttendanceDateDescAttendanceTimeDesc();

    // ==========================================
    // Department
    // ==========================================

    List<Attendance> findByStudent_Department(String department);

    // ==========================================
    // Status
    // ==========================================

    List<Attendance> findByStatus(String status);

    // ==========================================
    // Date + Department
    // ==========================================

    List<Attendance> findByAttendanceDateAndStudent_Department(
            LocalDate attendanceDate,
            String department
    );

    // ==========================================
    // Date + Status
    // ==========================================

    List<Attendance> findByAttendanceDateAndStatus(
            LocalDate attendanceDate,
            String status
    );

    // ==========================================
    // Department + Status
    // ==========================================

    List<Attendance> findByStudent_DepartmentAndStatus(
            String department,
            String status
    );

    // ==========================================
    // Date + Department + Status
    // ==========================================

    List<Attendance> findByAttendanceDateAndStudent_DepartmentAndStatus(
            LocalDate attendanceDate,
            String department,
            String status
    );

    // ==========================================
    // Attendance Analytics (Last 7 Days)
    // ==========================================

    @Query("""
            SELECT a.attendanceDate, COUNT(a)
            FROM Attendance a
            WHERE a.attendanceDate >= :startDate
            GROUP BY a.attendanceDate
            ORDER BY a.attendanceDate
            """)
    List<Object[]> getAttendanceAnalytics(
            @Param("startDate") LocalDate startDate
    );

}