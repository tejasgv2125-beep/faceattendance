package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.service.AttendanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ReportsController {

    private final AttendanceService attendanceService;

    public ReportsController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // ==========================================
    // Attendance Reports
    // ==========================================

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

        model.addAttribute(
                "attendanceList",
                attendanceService.filterAttendance(
                        date,
                        department,
                        status
                )
        );

        // Preserve selected filter values
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedStatus", status);

        return "reports";
    }

}