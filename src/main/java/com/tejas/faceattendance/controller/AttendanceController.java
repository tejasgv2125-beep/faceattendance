package com.tejas.faceattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttendanceController {

    @GetMapping("/live-attendance")
    public String liveAttendance() {
        return "liveAttendance";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "analytics";
    }

    @GetMapping("/downloads")
    public String downloads() {
        return "downloads";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}