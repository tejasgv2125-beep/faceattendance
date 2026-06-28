package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance saveAttendance(Attendance attendance){
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getTodayAttendance(){
        return attendanceRepository.findByAttendanceDate(LocalDate.now());
    }

    public boolean alreadyMarked(Student student){
        return attendanceRepository.existsByStudentAndAttendanceDate(
                student,
                LocalDate.now()
        );
    }
}