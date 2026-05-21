package com.cts.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.AuditLogDTO;
import com.cts.entity.AuditLog;
import com.cts.service.AuditService;


@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @PostMapping("/internal")
    public ResponseEntity<Void> saveAudit(@RequestBody AuditLogDTO dto) {
        dto.setTimestamp(LocalDateTime.now());
        service.save(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logs")
    public ResponseEntity<?> getLogs(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (role == null || (!role.equals("ADMIN") && !role.equals("AUDITOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or AUDITOR can view audit logs");
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/logs/user/{email}")
    public ResponseEntity<?> getLogsByUser(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable String email) {

        if (role == null || (!role.equals("ADMIN") && !role.equals("AUDITOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or AUDITOR can view user audit logs");
        }
        return ResponseEntity.ok(service.getByUsername(email));
    }
}
