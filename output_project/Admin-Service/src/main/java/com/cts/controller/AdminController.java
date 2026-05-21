package com.cts.controller;

import com.cts.repository.AdminRepository;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.ForgotPasswordRequest;
import com.cts.dto.ForgotUsernameRequest;
import com.cts.dto.LoginDTO;
import com.cts.dto.UserDTO;
import com.cts.dto.UserIdDTO;
import com.cts.dto.UserPromotionDTO;
import com.cts.entity.User;
import com.cts.service.AdminService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final AdminService adminService;

    public AdminController(AdminService adminService, AdminRepository adminRepository) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/addUsers")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.status(201).body(adminService.addUser(dto));
    }


    @PostMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@Valid @RequestBody LoginDTO dto) {
        String role = adminService.validateUser(dto);
        return ResponseEntity.ok(Map.of("role", role));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can view all users");
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {

        if (!"ADMIN".equals(role) && !"DISPATCHER".equals(role) && !"FLEET_MANAGER".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<?> getUserByEmail(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable String email) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can look up users by email");
        }
        return ResponseEntity.ok(adminService.findByEmail(email));
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<?> getByUsername(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable String username) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can look up users by name");
        }
        return ResponseEntity.ok(adminService.getByUsername(username));
    }

    @PutMapping("/users/{id}/updateRole")
    public ResponseEntity<?> updateRole(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id,
            @RequestBody UserPromotionDTO dto) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can update user roles");
        }
        adminService.updateRole(id, dto);
        return ResponseEntity.ok(Map.of("message", "User role updated"));
    }

    @PutMapping("/users/{id}/reactivate")
    public ResponseEntity<?> reactivate(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can reactivate users");
        }
        adminService.reactivateUser(id);
        return ResponseEntity.ok(Map.of("message", "User reactivated"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deactivate(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can deactivate users");
        }
        adminService.deactivateUser(id);
        return ResponseEntity.ok(Map.of("message", "User deactivated"));
    }

    @GetMapping("/shipment/users/{id}")
    public ResponseEntity<?> getUserIdForShipment(@PathVariable Long id) {
        User user = adminRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with ID: " + id));
        return ResponseEntity.ok(new UserIdDTO(user.getUserId()));
    }

    @GetMapping("/internal/users/{id}")
    public ResponseEntity<?> getUserInternal(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PostMapping("/auth/forgot-username")
    public ResponseEntity<?> forgotUsername(@Valid @RequestBody ForgotUsernameRequest request) {
        return ResponseEntity.ok(adminService.forgotUsername(request));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(adminService.forgotPassword(request));
    }
}
