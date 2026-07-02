package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    private static final String UPLOAD_DIR =
            "src/main/resources/static/uploads/students/";

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ===========================
    // Student List
    // ===========================

    @GetMapping
    public String viewStudents(Model model) {

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("totalStudents",
                studentService.getTotalStudents());

        model.addAttribute("faceRegistered",
                studentService.getRegisteredFaces());

        model.addAttribute("pendingRegistration",
                studentService.getPendingFaces());

        return "students";
    }

    // ===========================
    // Add Student Page
    // ===========================

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());

        return "addStudent";
    }

    // ===========================
    // Save Student
    // ===========================

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student,
                              @RequestParam("capturedImage") String capturedImage)
            throws IOException {

        if (capturedImage != null && !capturedImage.isBlank()) {

            String base64Image = capturedImage;

            if (capturedImage.contains(",")) {
                base64Image = capturedImage.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = UUID.randomUUID() + ".jpg";

            Files.write(
                    Paths.get(UPLOAD_DIR + fileName),
                    imageBytes
            );

            student.setPhotoPath("/uploads/students/" + fileName);
        }

        studentService.saveStudent(student);

        return "redirect:/students";
    }

    // ===========================
    // Edit Student
    // ===========================

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id,
                              Model model) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            return "redirect:/students";
        }

        model.addAttribute("student", student);

        return "addStudent";
    }

    // ===========================
    // Delete Student
    // ===========================

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return "redirect:/students";
    }

    // ===========================
    // Face Registration Page
    // ===========================

    @GetMapping("/register-face/{id}")
    public String registerFace(@PathVariable Long id,
                               Model model) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            return "redirect:/students";
        }

        model.addAttribute("student", student);

        return "faceRegistration";
    }

}