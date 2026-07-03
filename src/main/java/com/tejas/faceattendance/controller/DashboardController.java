package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.repository.AttendanceRepository;
import com.tejas.faceattendance.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardController(StudentRepository studentRepository,
                               AttendanceRepository attendanceRepository) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // ==========================
        // Student Statistics
        // ==========================

        long totalStudents = studentRepository.count();

        long registeredFaces = studentRepository.findAll()
                .stream()
                .filter(student ->
                        student.getFaceDescriptor() != null &&
                                !student.getFaceDescriptor().trim().isEmpty())
                .count();

        long pendingFaces = totalStudents - registeredFaces;

        // ==========================
        // Attendance Statistics
        // ==========================

        long presentToday =
                attendanceRepository.findByAttendanceDate(LocalDate.now()).size();

        long absentToday = totalStudents - presentToday;

        long todayAttendance = presentToday;

        // ==========================
        // Attendance Percentage
        // ==========================

        double attendancePercentage = 0;

        if (totalStudents > 0) {

            attendancePercentage =
                    (presentToday * 100.0) / totalStudents;

        }

        // ==========================
        // Send Data to Dashboard
        // ==========================

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("registeredFaces", registeredFaces);
        model.addAttribute("pendingFaces", pendingFaces);

        model.addAttribute("presentToday", presentToday);
        model.addAttribute("absentToday", absentToday);

        model.addAttribute("todayAttendance", todayAttendance);
        model.addAttribute("attendancePercentage",
                String.format("%.1f", attendancePercentage));

        return "dashboard";
    }
}