package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.service.AttendanceService;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public AttendanceController(AttendanceService attendanceService,
                                StudentService studentService) {

        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    // ==========================================
    // Test API
    // ==========================================

    @GetMapping("/test")
    public String test() {
        return "Attendance Controller Working";
    }

    // ==========================================
    // Mark Attendance
    // ==========================================

    @PostMapping("/mark/{studentId}")
    public ResponseEntity<String> markAttendance(@PathVariable Long studentId) {

        Student student = studentService.getStudentById(studentId);

        if (student == null) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        String message = attendanceService.markAttendance(student);

        return ResponseEntity.ok(message);
    }

}