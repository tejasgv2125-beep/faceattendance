package com.tejas.faceattendance.controller;

import com.tejas.faceattendance.entity.Admin;
import com.tejas.faceattendance.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final AdminService adminService;

    public SettingsController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ==========================================
    // Open Settings Page
    // ==========================================

    @GetMapping
    public String settings(Model model) {

        Admin admin = adminService.getLoggedInAdmin();

        model.addAttribute("admin", admin);

        return "settings";
    }

    // ==========================================
    // Update Profile
    // ==========================================

    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email) {

        adminService.updateProfile(name, email);

        return "redirect:/settings?success";
    }

    // ==========================================
    // Change Password
    // ==========================================

    @PostMapping("/change-password")
    public String changePassword(

            @RequestParam String currentPassword,

            @RequestParam String newPassword,

            @RequestParam String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {

            return "redirect:/settings?passwordMismatch";

        }

        boolean changed =
                adminService.changePassword(
                        currentPassword,
                        newPassword
                );

        if (!changed) {

            return "redirect:/settings?invalidPassword";

        }

        return "redirect:/settings?passwordChanged";

    }

}