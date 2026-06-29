package com.tejas.faceattendance.config;

import com.tejas.faceattendance.entity.Admin;
import com.tejas.faceattendance.entity.Role;
import com.tejas.faceattendance.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AdminRepository adminRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (adminRepository.findByUsername("admin").isEmpty()) {

            Admin admin = new Admin();

            admin.setName("Administrator");
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);

            adminRepository.save(admin);

            System.out.println("===================================");
            System.out.println("DEFAULT ADMIN CREATED");
            System.out.println("Username : admin");
            System.out.println("Password : admin123");
            System.out.println("===================================");
        }
    }
}