package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =====================================
    // Save Student
    // =====================================

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // =====================================
    // Get All Students
    // =====================================

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // =====================================
    // Get Student By ID
    // =====================================

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // =====================================
    // Get Student By USN
    // =====================================

    public Optional<Student> getStudentByUsn(String usn) {
        return studentRepository.findByUsn(usn);
    }

    // =====================================
    // Delete Student
    // =====================================

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // =====================================
    // Dashboard Statistics
    // =====================================

    public long getTotalStudents() {
        return studentRepository.count();
    }

    public long getRegisteredFaces() {

        return studentRepository.findAll()
                .stream()
                .filter(student ->
                        student.getFaceDescriptor() != null &&
                                !student.getFaceDescriptor().trim().isEmpty())
                .count();
    }

    public long getPendingFaces() {
        return getTotalStudents() - getRegisteredFaces();
    }

    // =====================================
    // Check Face Registration
    // =====================================

    public boolean isFaceRegistered(Long studentId) {

        Student student = getStudentById(studentId);

        if (student == null) {
            return false;
        }

        return student.getFaceDescriptor() != null
                && !student.getFaceDescriptor().trim().isEmpty();
    }

    // =====================================
    // Get Registered Students
    // =====================================

    public List<Student> getRegisteredStudents() {

        return studentRepository.findAll()
                .stream()
                .filter(student ->
                        student.getFaceDescriptor() != null &&
                                !student.getFaceDescriptor().trim().isEmpty())
                .toList();
    }

}