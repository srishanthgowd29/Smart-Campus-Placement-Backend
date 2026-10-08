package com.example.demo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class AdminController {

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;


    // =========================
    // ADMIN LOGIN
    // =========================

    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestBody AdminLoginData data,
            HttpSession session) {

        String adminUsername = "admin";

        String adminPasswordHash =
                passwordEncoder.encode("admin123");

        if (adminUsername.equals(data.getUsername())
                && passwordEncoder.matches(
                        data.getPassword(),
                        adminPasswordHash)) {

            session.setAttribute("admin", true);

            return "Admin login successful";
        }

        return "Invalid admin username or password";
    }


    // =========================
    // CHECK ADMIN LOGIN
    // =========================

    @GetMapping("/admin/check")
    public String checkAdmin(HttpSession session) {

        Boolean admin =
                (Boolean) session.getAttribute("admin");

        if (Boolean.TRUE.equals(admin)) {
            return "Admin authenticated";
        }

        return "Admin not authenticated";
    }


    // =========================
    // ADMIN LOGOUT
    // =========================

    @PostMapping("/admin/logout")
    public String adminLogout(HttpSession session) {

        session.invalidate();

        return "Admin logged out";
    }
}