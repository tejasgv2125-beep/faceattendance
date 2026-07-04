package com.tejas.faceattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/")
    public String loginPage(Model model) {



        return "adminLogin";
    }

//    @GetMapping("/admin/dashboard")
//    public String dashboard() {
//        return "dashboard";
//    }

}