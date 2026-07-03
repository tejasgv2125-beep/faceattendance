package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recognition")
public class FaceRecognitionController {

    private final StudentService studentService;

    public FaceRecognitionController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> getRegisteredStudents() {

        return studentService.getRegisteredStudents();

    }

}