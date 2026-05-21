package com.cts.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.*;
import com.cts.service.APIClient;
import com.cts.service.AuditFeignClient;
import com.cts.utility.JwtUtil;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private APIClient apiClient;

    @Autowired
    private AuditFeignClient auditClient;

    @Autowired
    private JwtUtil jwtUtil;

    // POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        apiClient.registerUser(request);

        sendAuditLog("REGISTER", "POST", request.getEmail(), "User registered as CUSTOMER");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully. Contact your admin to assign a role."));
    }

    // POST /auth/login
    // Role is fetched from the database via Admin-Service — never from the request body.
    @PostMapping("/login")
    @Retry(name = "API-Gateway", fallbackMethod = "loginFallback")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        // Feign call returns { "role": "ADMIN" } — role is from the DB
        Map<String, String> result = apiClient.getUserDetails(request);
        String roleFromDatabase = result.get("role");

        if (roleFromDatabase == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Bad credentials: check your email and password"));
        }

        // JWT is signed with the DB role — client cannot manipulate this
        String token = jwtUtil.generateToken(request.getEmail(), roleFromDatabase);

        sendAuditLog("LOGIN", "POST", request.getEmail(), "Login successful — role: " + roleFromDatabase);

        return ResponseEntity.ok(new AuthResponse(token, "Login Successful"));
    }

    // Fallback triggered when Admin-Service is unreachable (Resilience4j retry exhausted)
    public ResponseEntity<?> loginFallback(@Valid @RequestBody LoginRequest request, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", "Service temporarily unavailable. Please try again."));
    }

    // POST /auth/forgot-username
    // Returns the name associated with the email. No token required.
    @PostMapping("/forgot-username")
    public ResponseEntity<?> forgotUsername(@Valid @RequestBody ForgotUsernameRequest request) {

        String result = apiClient.forgotUsername(request);

        sendAuditLog("FORGOT_USERNAME", "POST", request.getEmail(), "Username recovery requested");

        return ResponseEntity.ok(Map.of("message", result));
    }

    // POST /auth/forgot-password
    // Resets the password for the account linked to the email. No token required.
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        String result = apiClient.forgotPassword(request);

        sendAuditLog("FORGOT_PASSWORD", "POST", request.getEmail(), "Password reset completed");

        return ResponseEntity.ok(Map.of("message", result));
    }

    // ── Private helper ────────────────────────────────────────────────────────
    private void sendAuditLog(String action, String method, String username, String details) {
        try {
            AuditLogDTO log = new AuditLogDTO();
            log.setServiceName("API-Gateway");
            log.setAction(action);
            log.setMethod(method);
            log.setUsername(username);
            log.setDetails(details);
            auditClient.sendLog(log);
        } catch (Exception e) {
            // Never let audit failure break the main flow
            System.err.println("Audit log failed: " + e.getMessage());
        }
    }
}
