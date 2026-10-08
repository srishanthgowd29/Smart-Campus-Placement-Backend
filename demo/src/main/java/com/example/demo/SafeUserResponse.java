package com.example.demo;

public class SafeUserResponse {

    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String branch;
    private Double cgpa;
    private String skills;
    private String resume;

    public SafeUserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.branch = user.getBranch();
        this.cgpa = user.getCgpa();
        this.skills = user.getSkills();
        this.resume = user.getResume();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBranch() {
        return branch;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public String getResume() {
        return resume;
    }
}