package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // ===========================
    // Mark Attendance
    // ===========================

    public String markAttendance(Student student) {

        LocalDate today = LocalDate.now();

        // Prevent duplicate attendance
        if (attendanceRepository.findByStudentAndAttendanceDate(student, today).isPresent()) {
            return "Attendance already marked today.";
        }

        Attendance attendance = new Attendance();

        attendance.setStudent(student);
        attendance.setAttendanceDate(today);
        attendance.setAttendanceTime(LocalTime.now());
        attendance.setStatus("Present");

        attendanceRepository.save(attendance);

        return "Attendance marked successfully.";
    }

    // ===========================
    // Get Today's Attendance
    // ===========================

    public List<Attendance> getTodayAttendance() {
        return attendanceRepository.findByAttendanceDate(LocalDate.now());
    }

    // ===========================
    // Get All Attendance
    // ===========================

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}