package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Admin;
import com.tejas.faceattendance.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin saveAdmin(Admin admin){
        return adminRepository.save(admin);
    }

    public Optional<Admin> login(String username){
        return adminRepository.findByUsername(username);
    }
}