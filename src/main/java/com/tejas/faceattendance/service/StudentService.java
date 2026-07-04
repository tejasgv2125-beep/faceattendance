package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.AttendanceRepository;
import com.tejas.faceattendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public StudentService(StudentRepository studentRepository,
                          AttendanceRepository attendanceRepository) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
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

    // =====================================
    // Student Profile Methods
    // =====================================

    public long getPresentDays(Student student) {

        return attendanceRepository.countByStudentAndStatus(
                student,
                "Present"
        );
    }

    public long getTotalAttendance(Student student) {

        return attendanceRepository.countByStudent(student);
    }

    public long getAbsentDays(Student student) {

        long totalAttendance = getTotalAttendance(student);

        long presentDays = getPresentDays(student);

        return totalAttendance - presentDays;
    }

    public double getAttendancePercentage(Student student) {

        long totalAttendance = getTotalAttendance(student);

        if (totalAttendance == 0) {
            return 0;
        }

        long presentDays = getPresentDays(student);

        return (presentDays * 100.0) / totalAttendance;
    }

    public List<Attendance> getAttendanceHistory(Student student) {

        return attendanceRepository
                .findByStudentOrderByAttendanceDateDescAttendanceTimeDesc(student);
    }

    // =====================================
    // Department Analytics
    // =====================================

    public List<Object[]> getDepartmentWiseStudentCount() {

        return studentRepository.getDepartmentWiseStudentCount();

    }

}