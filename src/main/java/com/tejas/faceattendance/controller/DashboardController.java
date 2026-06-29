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

        long totalStudents = studentRepository.count();

        long presentToday =
                attendanceRepository.findByAttendanceDate(LocalDate.now()).size();

        long absentToday = totalStudents - presentToday;

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("presentToday", presentToday);
        model.addAttribute("absentToday", absentToday);

        return "dashboard";
    }
}