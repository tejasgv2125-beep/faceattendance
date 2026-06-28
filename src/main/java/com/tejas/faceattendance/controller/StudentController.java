package com.tejas.faceattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/students")
    public String students() {
        return "students";
    }

    @GetMapping("/register-face")
    public String registerFace() {
        return "registerFace";
    }
}