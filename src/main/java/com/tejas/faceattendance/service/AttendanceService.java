package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Attendance;
import com.tejas.faceattendance.entity.Student;
import com.tejas.faceattendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // ==========================================
    // Mark Attendance
    // ==========================================

    public String markAttendance(Student student) {

        LocalDate today = LocalDate.now();

        if (attendanceRepository
                .findByStudentAndAttendanceDate(student, today)
                .isPresent()) {

            return "Attendance already marked today.";
        }

        Attendance attendance = new Attendance();

        attendance.setStudent(student);
        attendance.setAttendanceDate(today);
        attendance.setAttendanceTime(LocalTime.now());
        attendance.setStatus("Present");

        attendanceRepository.save(attendance);

        return "Attendance marked successfully.";
    }

    // ==========================================
    // Today's Attendance
    // ==========================================

    public List<Attendance> getTodayAttendance() {

        return attendanceRepository.findByAttendanceDate(LocalDate.now());

    }

    // ==========================================
    // All Attendance
    // ==========================================

    public List<Attendance> getAllAttendance() {

        return attendanceRepository
                .findAllByOrderByAttendanceDateDescAttendanceTimeDesc();

    }

    // ==========================================
    // Last 7 Days Analytics
    // ==========================================

    public List<Object[]> getLast7DaysAttendance() {

        LocalDate startDate = LocalDate.now().minusDays(6);

        return attendanceRepository.getAttendanceAnalytics(startDate);

    }

    // ==========================================
    // Reports Filter
    // ==========================================

    public List<Attendance> filterAttendance(
            LocalDate date,
            String department,
            String status) {

        boolean hasDate =
                date != null;

        boolean hasDepartment =
                department != null &&
                        !department.isBlank() &&
                        !department.equalsIgnoreCase("All");

        boolean hasStatus =
                status != null &&
                        !status.isBlank() &&
                        !status.equalsIgnoreCase("All");

        if (hasDate && hasDepartment && hasStatus) {

            return attendanceRepository
                    .findByAttendanceDateAndStudent_DepartmentAndStatus(
                            date,
                            department,
                            status
                    );
        }

        if (hasDate && hasDepartment) {

            return attendanceRepository
                    .findByAttendanceDateAndStudent_Department(
                            date,
                            department
                    );
        }

        if (hasDate && hasStatus) {

            return attendanceRepository
                    .findByAttendanceDateAndStatus(
                            date,
                            status
                    );
        }

        if (hasDepartment && hasStatus) {

            return attendanceRepository
                    .findByStudent_DepartmentAndStatus(
                            department,
                            status
                    );
        }

        if (hasDate) {

            return attendanceRepository
                    .findByAttendanceDate(date);
        }

        if (hasDepartment) {

            return attendanceRepository
                    .findByStudent_Department(department);
        }

        if (hasStatus) {

            return attendanceRepository
                    .findByStatus(status);
        }

        return attendanceRepository.findAll();
    }

}