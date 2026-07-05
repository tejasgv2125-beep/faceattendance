package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.service.AttendanceService;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportsController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public ReportsController(AttendanceService attendanceService,
                             StudentService studentService) {

        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    @GetMapping("/reports")
    public String reports(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String status,

            Model model) {

        List<Attendance> attendanceList =
                attendanceService.filterAttendance(
                        date,
                        department,
                        status
                );

        model.addAttribute("attendanceList", attendanceList);

        // Preserve Filters
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedStatus", status);

        // Statistics Cards
        model.addAttribute("totalReports", attendanceList.size());

        model.addAttribute(
                "totalStudents",
                studentService.getAllStudents().size()
        );

        if (!attendanceList.isEmpty()) {

            model.addAttribute(
                    "latestAttendance",
                    attendanceList.get(0).getAttendanceDate()
            );

        } else {

            model.addAttribute(
                    "latestAttendance",
                    "No Records"
            );

        }

        return "reports";
    }

}