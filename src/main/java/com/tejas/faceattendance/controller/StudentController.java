package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    // Folder where uploaded images will be stored
    private static final String UPLOAD_DIR =
            "src/main/resources/static/uploads/students/";

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Display all students
    @GetMapping
    public String viewStudents(Model model) {

        model.addAttribute("students", studentRepository.findAll());

        return "students";
    }

    // Add Student Page
    @GetMapping("/add")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());

        return "addStudent";
    }

    // Save Student
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student,
                              @RequestParam("photo") MultipartFile photo)
            throws IOException {

        if (!photo.isEmpty()) {

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = UUID.randomUUID() + "_"
                    + StringUtils.cleanPath(photo.getOriginalFilename());

            Path uploadPath = Paths.get(UPLOAD_DIR);

            Files.copy(photo.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            // Save only the relative path
            student.setPhotoPath("/uploads/students/" + fileName);
        }

        studentRepository.save(student);

        return "redirect:/students";
    }

    // Edit Student
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id,
                              Model model) {

        Student student =
                studentRepository.findById(id).orElseThrow();

        model.addAttribute("student", student);

        return "addStudent";
    }

    // Delete Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentRepository.deleteById(id);

        return "redirect:/students";
    }

}