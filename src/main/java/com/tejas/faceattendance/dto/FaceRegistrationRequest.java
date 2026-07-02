package com.tejas.faceattendance.dto;

import java.util.List;

public class FaceRegistrationRequest {

    private Long studentId;

    private List<Float> descriptor;

    public FaceRegistrationRequest() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public List<Float> getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(List<Float> descriptor) {
        this.descriptor = descriptor;
    }
}