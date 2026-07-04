package com.tejas.faceattendance.service;

import com.tejas.faceattendance.entity.Admin;
import com.tejas.faceattendance.repository.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;

    }

    // =====================================
    // Get Logged-in Admin
    // =====================================

    public Admin getLoggedInAdmin() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return adminRepository.findByUsername(username).orElse(null);

    }

    // =====================================
    // Update Profile
    // =====================================

    public void updateProfile(String name,
                              String email) {

        Admin admin = getLoggedInAdmin();

        if (admin == null) {
            return;
        }

        admin.setName(name);
        admin.setEmail(email);

        adminRepository.save(admin);

    }

    // =====================================
    // Change Password
    // =====================================

    public boolean changePassword(String currentPassword,
                                  String newPassword) {

        Admin admin = getLoggedInAdmin();

        if (admin == null) {
            return false;
        }

        if (!passwordEncoder.matches(currentPassword,
                admin.getPassword())) {

            return false;

        }

        admin.setPassword(passwordEncoder.encode(newPassword));

        adminRepository.save(admin);

        return true;

    }

}