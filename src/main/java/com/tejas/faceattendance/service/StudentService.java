package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student){
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByUsn(String usn){
        return studentRepository.findByUsn(usn);
    }

    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }
}