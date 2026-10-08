package com.example.demo;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.UserRepository;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Home
    @GetMapping("/d")
    public String home() {
        return "Home";
    }


    // Register
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        User existingUser =
                repository.findByUsername(user.getUsername());

        if (existingUser != null) {
            return "Username already exists";
        }

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        repository.save(user);

        return "Registration successful";
    }


    // Login
    @PostMapping("/login")
    public Object login(
            @RequestBody LoginData data,
            HttpSession session) {

        User user =
                repository.findByUsername(data.getUname());

        if (user == null) {
            return "Invalid username or password";
        }

        if (!passwordEncoder.matches(
                data.getPsw(),
                user.getPassword())) {

            return "Invalid username or password";
        }

        // Store logged-in username in session
        session.setAttribute(
                "username",
                user.getUsername()
        );

        // Do not return password
        return new LoginResponse(user);
    }


    // Get all users
    // Password is NOT returned
    @GetMapping("/users")
    public List<SafeUserResponse> getAllUsers() {

        return repository.findAll()
                .stream()
                .map(SafeUserResponse::new)
                .toList();
    }


    // Get user by username
    // Session protected
    @GetMapping("/user/{username}")
    public Object getUser(
            @PathVariable String username,
            HttpSession session) {

        String loggedInUsername =
                (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return "Please login first";
        }

        if (!loggedInUsername.equals(username)) {
            return "Access denied";
        }

        User user =
                repository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        return new SafeUserResponse(user);
    }


    // Update user profile
    // Session protected
    @PutMapping("/user/{username}")
    public String updateUser(
            @PathVariable String username,
            @RequestBody User updatedUser,
            HttpSession session) {

        String loggedInUsername =
                (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return "Please login first";
        }

        if (!loggedInUsername.equals(username)) {
            return "Access denied";
        }

        User user =
                repository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setBranch(updatedUser.getBranch());
        user.setCgpa(updatedUser.getCgpa());
        user.setSkills(updatedUser.getSkills());
        user.setResume(updatedUser.getResume());

        repository.save(user);

        return "Profile updated successfully";
    }


    // Update password
    // Session protected
    @PutMapping("/user/{username}/password")
    public String updatePassword(
            @PathVariable String username,
            @RequestBody User updatedUser,
            HttpSession session) {

        String loggedInUsername =
                (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return "Please login first";
        }

        if (!loggedInUsername.equals(username)) {
            return "Access denied";
        }

        User user =
                repository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        if (updatedUser.getPassword() == null ||
                updatedUser.getPassword().trim().isEmpty()) {

            return "Password cannot be empty";
        }

        // Encrypt new password
        user.setPassword(
                passwordEncoder.encode(
                        updatedUser.getPassword()
                )
        );

        repository.save(user);

        return "Password updated successfully";
    }


    // Delete user
    // Session protected
    @DeleteMapping("/user/{username}")
    public String deleteUser(
            @PathVariable String username,
            HttpSession session) {

        String loggedInUsername =
                (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return "Please login first";
        }

        if (!loggedInUsername.equals(username)) {
            return "Access denied";
        }

        User user =
                repository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        repository.delete(user);

        // End session after deleting account
        session.invalidate();

        return "User deleted successfully";
    }


    // Forgot Password
    @PutMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody ForgotPasswordData data) {

        User user =
                repository.findByUsername(data.getUsername());

        if (user == null) {
            return "Username not found";
        }

        if (user.getEmail() == null ||
                !user.getEmail().equals(data.getEmail())) {

            return "Email does not match";
        }

        if (data.getNewPassword() == null ||
                data.getNewPassword().trim().isEmpty()) {

            return "New password cannot be empty";
        }

        // Encrypt new password
        user.setPassword(
                passwordEncoder.encode(
                        data.getNewPassword()
                )
        );

        repository.save(user);

        return "Password reset successful";
    }

}