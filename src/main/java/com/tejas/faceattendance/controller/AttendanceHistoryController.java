package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.service.AttendanceService;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AttendanceHistoryController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public AttendanceHistoryController(
            AttendanceService attendanceService,
            StudentService studentService) {

        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    @GetMapping("/attendance/history")
    public String history(Model model) {

        List<Attendance> attendanceList =
                attendanceService.getAllAttendance();

        model.addAttribute("attendanceList", attendanceList);

        // Card 1
        model.addAttribute("totalRecords", attendanceList.size());

        // Card 2
        model.addAttribute(
                "totalStudents",
                studentService.getAllStudents().size()
        );

        // Card 3
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

        return "attendanceHistory";
    }

}