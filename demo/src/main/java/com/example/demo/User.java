
package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String phone;
    private String branch;
    private double cgpa;
    private String skills;
    private String resume;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getBranch() {
    return branch;
}

public void setBranch(String branch) {
    this.branch = branch;
}
public double getCgpa() {
    return cgpa;
}

public void setCgpa(double cgpa) {
    this.cgpa = cgpa;
}
public String getSkills() {
    return skills;
}

public void setSkills(String skills) {
    this.skills = skills;
}
public String getResume() {
    return resume;
}

public void setResume(String resume) {
    this.resume = resume;
}
}

