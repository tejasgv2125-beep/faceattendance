package com.tejas.faceattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/")
    public String loginPage() {
        return "adminLogin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

}