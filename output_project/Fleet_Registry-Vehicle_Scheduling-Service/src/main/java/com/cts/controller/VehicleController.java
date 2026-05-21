package com.cts.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.VehicleDTO;
import com.cts.entity.Vehicle;
import com.cts.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody Vehicle v) {
        if (!"ADMIN".equals(role) && !"FLEET_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or FLEET_MANAGER can register vehicles");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(v));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (role == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {
        if (role == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id, @Valid @RequestBody Vehicle v) {
        if (!"ADMIN".equals(role) && !"FLEET_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or FLEET_MANAGER can update vehicles");
        }
        return ResponseEntity.ok(service.update(id, v));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id, @RequestParam String status) {
        if (!"ADMIN".equals(role) && !"FLEET_MANAGER".equals(role) && !"DISPATCHER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN, FLEET_MANAGER or DISPATCHER can update vehicle status");
        }
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete vehicles");
        }
        service.delete(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}
