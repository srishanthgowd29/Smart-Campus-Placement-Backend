package com.example.demo;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.repository.ApplicationRepository;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class ApplicationController {

    @Autowired
    private ApplicationRepository ar;


    // =========================
    // STUDENT APPLY FOR JOB
    // =========================

    @PostMapping("/apply")
    public ResponseEntity<?> apply(
            @RequestBody Application a,
            HttpSession session) {

        // Check student login
        String loggedInUsername =
                (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return ResponseEntity.status(401)
                    .body("Please login first");
        }

        // Make sure student can only apply using
        // their own username
        if (!loggedInUsername.equals(a.getUsername())) {
            return ResponseEntity.status(403)
                    .body("Access denied");
        }

        Application existing =
                ar.findByUsernameAndJobId(
                        a.getUsername(),
                        a.getJobId()
                );

        if (existing != null) {
            return ResponseEntity.badRequest()
                    .body("You have already applied for this job");
        }

        a.setStatus("Applied");

        a.setAppliedDate(
                java.time.LocalDate.now().toString()
        );

        return ResponseEntity.ok(
                ar.save(a)
        );
    }


    // =========================
    // GET APPLICATIONS
    // ADMIN = ALL APPLICATIONS
    // STUDENT = OWN APPLICATIONS
    // =========================

    @GetMapping("/applications")
    public ResponseEntity<?> getApplications(
            HttpSession session) {

        Boolean admin =
                (Boolean) session.getAttribute("admin");

        String username =
                (String) session.getAttribute("username");


        // Admin can see all applications
        if (Boolean.TRUE.equals(admin)) {

            return ResponseEntity.ok(
                    ar.findAll()
            );
        }


        // Student must be logged in
        if (username == null) {

            return ResponseEntity.status(401)
                    .body("Please login first");
        }


        // Student can see only their own applications
        List<Application> myApplications =
                ar.findAll()
                        .stream()
                        .filter(app ->
                                username.equals(
                                        app.getUsername()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(
                myApplications
        );
    }


    // =========================
    // UPDATE APPLICATION STATUS
    // ADMIN ONLY
    // =========================

    @PutMapping("/application/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable int id,
            @RequestBody Application updatedApplication,
            HttpSession session) {


        Boolean admin =
                (Boolean) session.getAttribute("admin");


        // Only admin can update status
        if (!Boolean.TRUE.equals(admin)) {

            return ResponseEntity.status(403)
                    .body("Admin authentication required");
        }


        Application existing =
                ar.findById(id).orElse(null);


        if (existing != null) {

            existing.setStatus(
                    updatedApplication.getStatus()
            );

            ar.save(existing);

            return ResponseEntity.ok(
                    existing
            );

        } else {

            return ResponseEntity.status(404)
                    .body("Application not found");
        }
    }
}