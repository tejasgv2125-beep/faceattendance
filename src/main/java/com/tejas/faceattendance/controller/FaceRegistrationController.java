package com.tejas.faceattendance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejas.faceattendance.dto.FaceRegistrationRequest;
import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faces")
public class FaceRegistrationController {

    private final StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FaceRegistrationController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ===============================
    // Register Face Descriptor
    // ===============================

    @PostMapping("/register")
    public ResponseEntity<String> registerFace(
            @RequestBody FaceRegistrationRequest request) {

        Student student =
                studentService.getStudentById(request.getStudentId());

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body("Student not found.");
        }

        try {

            System.out.println("========== FACE REGISTRATION ==========");

            System.out.println("Student ID : " + request.getStudentId());

            System.out.println("Descriptor Size : " + request.getDescriptor().size());

            String descriptorJson =
                    objectMapper.writeValueAsString(request.getDescriptor());

            student.setFaceDescriptor(descriptorJson);

            studentService.saveStudent(student);

            System.out.println("Face Descriptor Saved Successfully");

            return ResponseEntity.ok("Face registered successfully.");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(e.getMessage());

        }
    }

}