package com.example.demo;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.JobRepository;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class JobController {

    @Autowired
    private JobRepository jr;


    // =========================
    // ADD JOB
    // ADMIN ONLY
    // =========================

    @PostMapping("/job")
    public ResponseEntity<?> addJob(
            @RequestBody Job j,
            HttpSession session) {

        Boolean admin =
                (Boolean) session.getAttribute("admin");

        if (!Boolean.TRUE.equals(admin)) {

            return ResponseEntity.status(403)
                    .body("Admin authentication required");
        }

        return ResponseEntity.ok(
                jr.save(j)
        );
    }


    // =========================
    // VIEW JOBS
    // PUBLIC
    // =========================

    @GetMapping("/jobs")
    public List<Job> getJobs() {

        return jr.findAll();
    }
}