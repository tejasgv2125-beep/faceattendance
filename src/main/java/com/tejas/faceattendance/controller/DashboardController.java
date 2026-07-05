package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.repository.AttendanceRepository;
import com.tejas.faceattendance.repository.StudentRepository;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tejas.faceattendance.service.AttendanceService;
import com.tejas.faceattendance.entity.Admin;
import com.tejas.faceattendance.service.AdminService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final AttendanceService attendanceService;
    private final AdminService adminService;
    public DashboardController(StudentRepository studentRepository,
                               AttendanceRepository attendanceRepository,
                               StudentService studentService,
                               AttendanceService attendanceService,
                               AdminService adminService) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentService = studentService;
        this.attendanceService = attendanceService;
        this.adminService = adminService;
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
        // Department Analytics
        // ==========================

        List<Object[]> departmentData =
                studentService.getDepartmentWiseStudentCount();

        List<String> departments = new ArrayList<>();

        List<Long> departmentCounts = new ArrayList<>();

        for (Object[] row : departmentData) {

            departments.add((String) row[0]);

            departmentCounts.add((Long) row[1]);

        }
        // ==========================
// Last 7 Days Attendance Analytics
// ==========================

        List<Object[]> analyticsData =
                attendanceService.getLast7DaysAttendance();

        List<String> attendanceDates = new ArrayList<>();

        List<Long> attendanceCounts = new ArrayList<>();

        for (Object[] row : analyticsData) {

            attendanceDates.add(row[0].toString());

            attendanceCounts.add((Long) row[1]);

        }
//        List<Attendance> recentAttendance =
//                attendanceRepository
//                        .findTop5ByOrderByAttendanceDateDescAttendanceTimeDesc();
//
//        model.addAttribute("recentAttendance", recentAttendance);


        // ==========================
        // Send Data to Dashboard
        // ==========================

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("registeredFaces", registeredFaces);
        model.addAttribute("pendingFaces", pendingFaces);

        model.addAttribute("presentToday", presentToday);
        model.addAttribute("absentToday", absentToday);

        model.addAttribute("todayAttendance", todayAttendance);

        model.addAttribute(
                "attendancePercentage",
                String.format("%.1f", attendancePercentage)
        );

        model.addAttribute("departments", departments);
        model.addAttribute("departmentCounts", departmentCounts);
        model.addAttribute("attendanceDates", attendanceDates);
        model.addAttribute("attendanceCounts", attendanceCounts);
        Admin admin = adminService.getLoggedInAdmin();

        model.addAttribute("admin", admin);
        return "dashboard";
    }

}