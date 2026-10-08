package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {

}