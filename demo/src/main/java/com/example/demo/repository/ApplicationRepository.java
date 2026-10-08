package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    Application findByUsernameAndJobId(String username, int jobId);
}