package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveAttendanceController {

    @GetMapping("/attendance")
    public String attendance() {

        return "attendance";

    }

}