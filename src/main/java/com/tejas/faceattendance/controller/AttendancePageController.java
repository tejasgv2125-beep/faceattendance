package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendancePageController {

    private final AttendanceService attendanceService;

    public AttendancePageController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance")
    public String attendancePage() {
        return "attendance";
    }

    @GetMapping("/attendance/history")
    public String attendanceHistory(Model model) {

        model.addAttribute("attendanceList",
                attendanceService.getAllAttendance());

        return "attendanceHistory";
    }

}